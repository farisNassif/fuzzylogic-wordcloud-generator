package ie.gmit.sw.ai.search;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ie.gmit.sw.ai.categorical.Categorize;
import ie.gmit.sw.ai.cloud.WeightedFont;
import ie.gmit.sw.ai.cloud.WordFrequency;
import ie.gmit.sw.ai.fuzzylogic.BeamFuzzy;
import ie.gmit.sw.ai.fuzzylogic.BestFirstFuzzy;
import ie.gmit.sw.ai.util.GetFrequency;
import ie.gmit.sw.ai.util.IgnoreWords;
import ie.gmit.sw.ai.util.MapSort;
import ie.gmit.sw.ai.util.Stopwatch;

/* Handles the internal processing of the wordcloud */
public class BeamSearch extends Search {
	private final int TIME_LIMIT = 4;

	private Wordcloud wordcloud;
	private int branchingFactor;
	private int maxDepth;

	Stopwatch stopwatch = new Stopwatch();

	/* Contains URL's that were already visited */
	private Set<String> closed_list = new ConcurrentSkipListSet<>();
	/* Prio LIFO queue, unlike BFS this is sorted highest < lowest */
	private Queue<Node> queue = new PriorityQueue<>(Comparator.comparing(Node::getScore));
	/* Maps each polled node's words to their frequencies */
	private Map<String, Integer> word_freq = new ConcurrentHashMap<String, Integer>();

	public BeamSearch(Wordcloud wordcloud, int branchingFactor, int maxDepth) {
		this.wordcloud = wordcloud;
		this.branchingFactor = branchingFactor;
		this.maxDepth = maxDepth;
	}

	@Override
	public WordFrequency[] ExecuteSearch() {
		stopwatch.start();

		InitializeSearch();

		stopwatch.stop();
		System.out.println("Search finished in " + stopwatch.toString() + " seconds");

		/* Before returning with frequency array, categorize the query word */
		Categorize.category((word_freq));

		/* Generate freq table & sort word map highest > lowest & return it */
		return new WeightedFont()
				.getFontSizes(GetFrequency.GenerateFrequency((MapSort.crunchifySortMap(word_freq)), wordcloud));
	}

	/* Kicks off the search */
	private void InitializeSearch() {
		IgnoreWords.ignoreQuery(wordcloud.getWord());

		/* Construct the initial node */
		String queryUrl = "https://duckduckgo.com/html/?q=" + wordcloud.getWord();

		Node initial = new Node(queryUrl, 0);

		GenerateChildNodes(initial);
	}

	/* Generate children from a parent node */
	private void GenerateChildNodes(Node parent) {
		int birthControl = 0;
		Elements children = ConnectNode(parent).select("a");

		/* Score each child, add to prio queue */
		for (Element child : children) {
			String link = child.attr("href");

			if (!closed_list.contains(link) && link.startsWith("https://") && birthControl < branchingFactor) {
				birthControl++;
				/* New child, one level deeper than parent */
				Node childNode = new Node(link, parent, parent.getDepth() + 1);

				/* Dirty, but jsoup was giving me hastle */
				try {
					ScoreChild(childNode);
				} catch (NullPointerException e) {

				}

				/* Don't want to visit this url again */
				closed_list.add(link);
			}
		}

		if (closed_list.size() < (branchingFactor * maxDepth + 1)) {
			/* For each node currently in the queue (Max 2) */
			for (Node node : queue) {
				/* If this node hasn't been expanded before .. */
				if (node.isTraversed == false) {
					/* Mark it as traversed, and generate children from it */
					node.isTraversed = true;
					GenerateChildNodes(node);
				}
			}
		}

		/* This'll only ever execute when search has ran its course */
		/* Will execute twice, one for each node in the queue */
		for (Node node : queue) {
			MapWords(node);
			/* Don't even really need to poll, but just incase */
			queue.poll();
		}
	}

	/* Take a child node, score it */
	private void ScoreChild(Node child) {
		/* Connect to child URL */
		Document childDoc = ConnectNode(child);

		/* Get Title data without numbers and symbols */
		String title = childDoc.title().replaceAll("[^a-zA-Z]+", " ").toLowerCase();

		/* Get Heading data without numbers and symbols */
		String headings = childDoc.select("h1,h2,h3").text().replaceAll("[^a-zA-Z]+", " ").toLowerCase();

		/* Get Paragraph data without numbers and symbols */
		String paragraph = childDoc.select("p").text().replaceAll("[^a-zA-Z]+", " ").toLowerCase();

		System.out.println(child.getUrl() + ": Heuristic Score => "
				+ BeamFuzzy.UrlRelevance(child, queue, title, headings, paragraph, wordcloud.getWord()) + " Depth: "
				+ child.getDepth());

		/* Score the child using fuzzy logic and set the score */
		child.setScore(BeamFuzzy.UrlRelevance(child, queue, title, headings, paragraph, wordcloud.getWord()));

		/* Always make sure at least 2 nodes in the queue */
		if (queue.size() < 2) {
			queue.offer(child);
		}

		/* Sometimes pages with different URL's but the same content are read */
		if (queue.peek().getScore() == child.getScore()) {
			return;
		}

		/* If child score matches any of the enqueued node scores */
		for (Node node : queue) {
			/* Should never ever match, ridiculously unlikely */
			if (node.getScore() == child.getScore()) {
				/* Go back */
				return;
			}
		}

		/* If lowest scoring node is less than the newly generated child */
		if (queue.peek().getScore() < child.getScore()) {
			/* Get rid of lowest child, add new child */
			queue.poll();
			queue.offer(child);
		}
	}

	/* Maps all words on the highest scoring page to frequency */
	private void MapWords(Node bestChild) {
		/* Connect to the best child */
		Document bestChildDoc = ConnectNode(bestChild);

		/* Get the whole text without symbold or numbers */
		String wholetext = bestChildDoc.wholeText().replaceAll("[^a-zA-Z]", " ").toLowerCase();
		String[] words = wholetext.split(" ");

		/* For each word .. */
		for (String word : words) {
			try {
				/* If it's not worthless and irrelevant .. */
				if ((word.length() > 2) && (!IgnoreWords.ignoreWords().contains(word))) {
					/* And if the word was already mapped */
					if (word_freq.containsKey(word)) {
						/* If it was encountered before, increment */
						word_freq.replace(word, word_freq.get(word), word_freq.get(word) + 1);
					} else {
						/* Otherwise this was the first time encountering it, put into map */
						word_freq.put(word, 1);
					}
				}
			} catch (IOException e) {

			}
		}
	}

	/* Connects to a child node, return Doc */
	private Document ConnectNode(Node child_to_connect_to) {
		Document doc = null;
		try {
			doc = Jsoup.connect(child_to_connect_to.getUrl()).userAgent(
					"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
					.referrer("http://www.google.com").ignoreContentType(true).ignoreHttpErrors(true).execute().parse();
		} catch (IOException e) {

		}
		return doc;
	}
}
