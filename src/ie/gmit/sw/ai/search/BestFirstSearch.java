package ie.gmit.sw.ai.search;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ie.gmit.sw.ai.categorical.Categorize;
import ie.gmit.sw.ai.cloud.WeightedFont;
import ie.gmit.sw.ai.cloud.WordFrequency;

/* Handles the internal processing of the wordcloud */
public class BestFirstSearch extends Search {
	/* Wordcloud object containing query word, branching factor and max depth */
	private Wordcloud wordcloud;
	private int branchingFactor;
	private int maxDepth;
	/* Contains URL's that were already visited */
	private Set<String> closed_list = new ConcurrentSkipListSet<>();
	/* Prio queue, polled after each branch, sorted highest > lowest */
	private Queue<Node> queue = new PriorityQueue<>(Comparator.comparing(Node::getScore).reversed());
	/* Maps each polled node's words to their frequencies */
	private Map<String, Integer> word_freq = new ConcurrentHashMap<String, Integer>();

	public BestFirstSearch(Wordcloud wordcloud, int branchingFactor, int maxDepth) {
		this.wordcloud = wordcloud;
		this.branchingFactor = branchingFactor;
		this.maxDepth = maxDepth;
	}

	public WordFrequency[] ExecuteSearch() {
		/* Start processing */
		try {
			InitializeSearch();
		} catch (Throwable e) {
			e.printStackTrace();
		}

		/* Generate freq table & sort word map highest > lowest */
		WordFrequency[] words = new WeightedFont().getFontSizes(GenerateFrequency(MapSort.crunchifySortMap(word_freq)));

		/* Before return with frequency array, categorize the query word */
		Categorize.findCategory(word_freq);

		return words;
	}

	/* Kicks off the search */
	private void InitializeSearch() throws Throwable {
		IgnoreWords.ignoreQuery(wordcloud.word);

		/* Construct the initial node */
		String queryUrl = "https://duckduckgo.com/html/?q=" + wordcloud.word;

		Node initial = new Node(queryUrl, 0);

		try {
			GenerateChildNodes(initial);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/* Generate children from a parent node */
	private void GenerateChildNodes(Node parent) throws Throwable {
		int count = 0;
		Elements children = null;

		try {
			children = ConnectNode(parent).select("a");
		} catch (IOException e) {
			e.printStackTrace();
		}

		/* Score each child, add to prio queue */
		for (Element child : children) {
			String link = child.attr("href");

			if (!closed_list.contains(link) && link.contains("https://") && count < branchingFactor) {
				count++;
				/* New child, one level deeper than parent */
				Node childNode = new Node(link, parent.getDepth() + 1);

				ScoreChild(childNode);

				/* Don't want to visit this url again */
				closed_list.add(link);
			}
		}

		/* Poll the queue, generate more children from the best child */
		if (closed_list.size() < (branchingFactor * maxDepth + 1) && queue.peek().getDepth() < maxDepth) {
			System.out.println("**POLLING** URL: " + queue.peek().getUrl() + " Depth: " + queue.peek().getDepth()
					+ " Score: " + queue.peek().getScore());
			/* Map words to frequencies for best child */
			MapWords(queue.peek());
			/* Remove from queue and go generate more children from the best child node */
			GenerateChildNodes(queue.poll());
		}
	}

	/* Take a child node, score it */
	private void ScoreChild(Node child) throws Throwable {
		/* Connect to child URL */
		Document childDoc = null;

		try {
			childDoc = ConnectNode(child);
		} catch (IOException e) {
			e.printStackTrace();
		}

		/* Get Title data without numbers and symbols */
		String title = childDoc.title().replaceAll("[^a-zA-Z]+", " ").toLowerCase();

		/* Get Heading data without numbers and symbols */
		String headings = childDoc.select("h1,h2,h3").text().replaceAll("[^a-zA-Z]+", " ").toLowerCase();

		/* Get Paragraph data without numbers and symbols */
		String paragraph = childDoc.select("p").text().replaceAll("[^a-zA-Z]+", " ").toLowerCase();

		System.out.println(child.getUrl() + ": Heuristic Score => "
				+ BestFirstSearchFuzzy.UrlRelevance(child, title, headings, paragraph, wordcloud.word) + " Depth: "
				+ child.getDepth());
		child.setScore(BestFirstSearchFuzzy.UrlRelevance(child, title, headings, paragraph, wordcloud.word));

		queue.offer(child);
	}

	/* Maps all words on the highest scoring page to frequency */
	private void MapWords(Node bestChild) throws Throwable {
		/* Connect to the best child */
		Document bestChildDoc = null;

		try {
			bestChildDoc = ConnectNode(bestChild);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		/* Get the whole text without symbold or numbers */
		String wholetext = bestChildDoc.wholeText().replaceAll("[^a-zA-Z]", " ").toLowerCase();
		String[] words = wholetext.split(" ");

		/* For each word .. */
		for (String word : words) {
			try {
				/* If it's not worthless and irrelevant .. */
				if ((word.length() > 2) && (!IgnoreWords.ignoreWords().contains(word))) {
					/* If the word was already mapped */
					if (word_freq.containsKey(word)) {
						/* If it was encountered before, increment */
						word_freq.replace(word, word_freq.get(word), word_freq.get(word) + 1);
					} else {
						/* If this was the first time encountering it, put into map */
						word_freq.put(word, 1);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/* Generate frequency table and return it to service handler */
	private WordFrequency[] GenerateFrequency(Map<String, Integer> sortedFrequencyMap) {
		int count = 0;
		WordFrequency[] wf = new WordFrequency[wordcloud.maxWords];

		for (Entry<String, Integer> word : sortedFrequencyMap.entrySet()) {
			if (count >= wordcloud.maxWords) {
				break;
			} else {
				wf[count] = new WordFrequency(word.getKey(), word.getValue());
				count++;
			}
		}
		return wf;
	}

	/* Basically at a low level connects to a URL string, return Doc */
	private Document ConnectNode(Node child_to_connect_to) throws Throwable {
		Document doc = null;
		try {
			doc = Jsoup.connect(child_to_connect_to.getUrl()).userAgent(
					"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
					.referrer("http://www.google.com").execute().parse();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
}
