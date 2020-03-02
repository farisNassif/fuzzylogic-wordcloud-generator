package ie.gmit.sw.ai.V3;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ie.gmit.sw.ai.V3.Document_Weights.Weight_Names;

public class RunnerV3 {
	static String initialUrl = "https://duckduckgo.com/html/?q=";

	private Set<String> closed_list = new ConcurrentSkipListSet<>();
	private static Document_Weights weight_mapping = new Document_Weights();
	private Queue<DocumentNode> priority_queue = new PriorityQueue<>(Comparator.comparing(DocumentNode::getScore));

	public static void main(String[] args) throws Throwable {

		for (Entry<Weight_Names, Integer> m : weight_mapping.Weight_Value_Mapping().entrySet()) {
			System.out.println(m.getKey() + " " + m.getValue());
		}

		String initialQuery = "samsung";

		new RunnerV3(initialUrl, initialQuery);

	}

	public RunnerV3(String url, String query) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url + query).userAgent("Mozilla").get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		closed_list.add(url);
		int score = getHeuristicScore(doc);

		priority_queue.offer(new DocumentNode(doc, score));
		try {
			process(query);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private int getHeuristicScore(Document doc) {
		int score = 0;

		String title = doc.title();
		score += getFrequency(title);
		System.out.println(closed_list.size() + "-->" + title);

		Elements headings = doc.select("h1");
		for (Element heading : headings) {
			String h1 = heading.text();
			score += getFrequency(h1);
			System.out.println("\t" + h1);
		}

		// String body = doc.body().text();
		// score += getFrequency(body) + PARAGRAPH_WEIGHT;

		return 0;
	}

	private int getFrequency(String s) {
		// Check for term in s
		return 1;
	}

	public void process(String query) {
		while (!priority_queue.isEmpty() && closed_list.size() <= 100) {
			DocumentNode node = priority_queue.poll();
			Document doc = node.getDocument();

			Elements edges = doc.select("a[href]");

			for (Element e : edges) {
				String link = e.absUrl("href");

				if (link != null && !closed_list.contains(link) && closed_list.size() <= 100 && link.contains("https")
						&& !link.contains("linkedin") && !link.contains(".png")) {

					closed_list.add(link);
					System.out.println(link);
					Document child_node = null;
					try {
						child_node = Jsoup.connect(link).get();
						
					} catch (IOException e1) {

					}

					int score = getHeuristicScore(child_node);
					priority_queue.offer(new DocumentNode(child_node, score));

				}

			}
		}
	}
}
