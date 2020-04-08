package complete.deploy;

import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/* Handles the internal processing of the wordcloud */
public class WordCloudProcessor implements Runnable {
	/* Wordcloud object containing query word, branching factor and max depth */
	private Wordcloud wordcloud;
	/* Contains URL's that were already visited */
	private Set<String> closed_list = new ConcurrentSkipListSet<>();
	private Queue<Node> queue = new PriorityQueue<>(Comparator.comparing(Node::getScore).reversed());
	private Set<Node> nodes = new ConcurrentSkipListSet<>();

	public WordCloudProcessor(Wordcloud wordcloud) {
		super();
		this.wordcloud = wordcloud;
	}

	@Override
	public void run() {
		System.out.println("Processing wordcloud ...");

		/* Start processing */
		InitializeSearch();
		while (!queue.isEmpty()) {
			// System.out.println("URL: " + queue.peek().getUrl() + " Depth: " +
			// queue.peek().getDepth() + " Score: " + queue.peek().getScore());
			queue.poll();
		}
		System.out.println("Finished");
	}

	/* Kicks off the search */
	private void InitializeSearch() {
		String queryUrl = "https://duckduckgo.com/html/?q=" + wordcloud.word;

		Node initial = new Node(queryUrl, 0);
		nodes.add(initial);

		GenerateChildren(initial);
	}

	private void GenerateChildren(Node parent) {
		int count = 0;

		Document doc = null;
		try {
			doc = Jsoup.connect(parent.getUrl()).get();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Elements children = doc.select("a");

		for (Element child : children) {
			String link = child.attr("href");

			if (!closed_list.contains(link) && link.contains("https://") && count < wordcloud.brachingFactor) {
				Node childNode = new Node(link, parent.getDepth() + 1);

				closed_list.add(link);

				ScoreChild(childNode);
				count++;
			}
		}
		if (count < 9) {
			System.out.println("**POLLING** URL: " + queue.peek().getUrl() + " Depth: " + queue.peek().getDepth()
					+ " Score: " + queue.peek().getScore());
			GenerateChildren(queue.poll());
		}
	}

	/* Takes a child URL and will score it based on relevance */
	private void ScoreChild(Node child) {
		/* Connec to child URL */
		Document doc = null;

		try {
			doc = Jsoup.connect(child.getUrl()).get();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/* Get Title data without numbers and symbols */
		String title = doc.title().replaceAll("[^a-zA-Z]+", " ").toLowerCase();

		/* Get Heading data without numbers and symbols */
		String headings = doc.select("h1,h2,h3").text().replaceAll("[^a-zA-Z]+", " ").toLowerCase();

		/* Get Paragraph data without numbers and symbols */
		String paragraph = doc.select("p").text().replaceAll("[^a-zA-Z]+", " ").toLowerCase();

		System.out.println(child.getUrl() + ": Heuristic Score => "
				+ RelevanceCalculator.UrlRelevance(child, title, headings, paragraph, wordcloud.word) + " Depth: " + child.getDepth());

		child.setScore(RelevanceCalculator.UrlRelevance(child, title, headings, paragraph, wordcloud.word));
		/* Map this URL and it's heuristic score, shove it onto priority queue */
		queue.offer(child);
	}
}
