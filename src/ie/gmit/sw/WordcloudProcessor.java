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
		Elements children = ConnectNode(parent).select("a");

		for (Element child : children) {
			String link = child.attr("href");
			if (!closed_list.contains(link) && link.contains("https://")) {

				Node childNode = new Node(link, parent.getDepth() + 1);

				ScoreChild(childNode);

				closed_list.add(link);
			}
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
