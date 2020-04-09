package ie.gmit.sw;

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

/* Handles the internal processing of the wordcloud */
public class WordcloudProcessor implements Runnable {
	/* Wordcloud object containing query word, branching factor and max depth */
	private Wordcloud wordcloud;
	/* Contains URL's that were already visited */
	private Set<String> closed_list = new ConcurrentSkipListSet<>();
	private Queue<Node> queue = new PriorityQueue<>(Comparator.comparing(Node::getScore).reversed());
	private Map<String, Integer> word_freq = new ConcurrentHashMap<String, Integer>();

	public WordcloudProcessor(Wordcloud wordcloud) {
		super();
		this.wordcloud = wordcloud;
	}

	@Override
	public void run() {
		/* Don't want the wordcloud to contain the query word */
		IgnoreWords.ignoreQuery(wordcloud.word);
		System.out.println("Processing wordcloud ...");

		/* Start processing */
		InitializeSearch();

		/* Sort word map highest > lowest */
		word_freq = MapSort.crunchifySortMap(word_freq);

		System.out.println(word_freq.entrySet());
		System.out.println("Finished");
	}

	/* Kicks off the search */
	private void InitializeSearch() {
		/* Construct the initial node */
		String queryUrl = "https://duckduckgo.com/html/?q=" + wordcloud.word;

		Node initial = new Node(queryUrl, 0);

		GenerateChildNodes(initial);
	}

	/* Generate children from a parent node */
	private void GenerateChildNodes(Node parent) {
		int count = 0;
		Elements children = ConnectNode(parent).select("a");

		/* Score each child, add to prio queue */
		for (Element child : children) {
			String link = child.attr("href");

			if (!closed_list.contains(link) && link.contains("https://") && count < wordcloud.brachingFactor) {
				count++;
				/* New child, one level deeper than parent */
				Node childNode = new Node(link, parent.getDepth() + 1);

				ScoreChild(childNode);

				/* Don't want to visit this url again */
				closed_list.add(link);
			}
		}

		/* Poll the queue, generate more children from the best child */
		if (closed_list.size() < 20 && queue.peek().getDepth() > wordcloud.maxDepth) {
			System.out.println("**POLLING** URL: " + queue.peek().getUrl() + " Depth: " + queue.peek().getDepth()
					+ " Score: " + queue.peek().getScore());
			
			/* Map words to frequencies for best child */
			MapWords(queue.peek());
			/* Remove from queue and go generate more children from the best child node */
			GenerateChildNodes(queue.poll());
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
				+ RelevanceCalculator.UrlRelevance(child, title, headings, paragraph, wordcloud.word) + " Depth: "
				+ child.getDepth());
		child.setScore(RelevanceCalculator.UrlRelevance(child, title, headings, paragraph, wordcloud.word));

		queue.offer(child);
	}

	/* Maps all words on the highest scoring page to frequency */
	private void MapWords(Node bestChild) {
		/* Connect to the best child */
		Document bestChildDoc = ConnectNode(bestChild);

		/* Get the whole text without symbolds or numbers */
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

	/* Basically at a low level connects to a URL string, return Doc */
	private Document ConnectNode(Node node_to_connect_to) {
		Document doc = null;
		try {
			doc = Jsoup.connect(node_to_connect_to.getUrl()).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
}
