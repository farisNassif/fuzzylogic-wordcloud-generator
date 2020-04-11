package ie.gmit.sw.search.test;

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

import ie.gmit.sw.ai.cloud.WeightedFont;
import ie.gmit.sw.ai.cloud.WordFrequency;

public class BeamSearch implements Runnable {
	/* Wordcloud object containing query word, branching factor and max depth */
	private Wordcloud wordcloud;
	/* Contains URL's that were already visited */
	private Set<String> closed_list = new ConcurrentSkipListSet<>();
	private Queue<Node> queue = new PriorityQueue<>(Comparator.comparing(Node::getScore).reversed());
	private Map<String, Integer> word_freq = new ConcurrentHashMap<String, Integer>();

	public BeamSearch(Wordcloud wordcloud) {
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

	private void GenerateChildNodes(Node parent) {
		int count = 0;
		Elements children = ConnectNode(parent).select("a");
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

		GenerateChildNodes(queue.poll());
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
				+ BeamSearchFuzzy.UrlRelevance(child, title, headings, paragraph, wordcloud.word) + " Depth: "
				+ child.getDepth());
		child.setScore(BeamSearchFuzzy.UrlRelevance(child, title, headings, paragraph, wordcloud.word));

		/* If queue is empty, add a node */
		if (queue.size() < 2) {
			queue.offer(child);
		} else {
			/* Only keeping two nodes on the queue at a time */
			if (queue.peek().getScore() < child.getScore()) {
				queue.poll();
				queue.offer(child);
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
