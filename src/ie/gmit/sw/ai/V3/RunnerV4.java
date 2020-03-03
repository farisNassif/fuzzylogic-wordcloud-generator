package ie.gmit.sw.ai.V3;

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

import ie.gmit.sw.ai.V4.Document_Weights;

public class RunnerV4 {
	private static Set<String> closed_list = new ConcurrentSkipListSet<>();
	private static Document_Weights weight_mapping = new Document_Weights();
	private static Queue<DocumentNode> priority_queue = new PriorityQueue<>(Comparator.comparing(DocumentNode::getScore));

	public static void main(String[] args) throws Exception {
		String query = "haha";
		String initialUrl = "https://duckduckgo.com/html/?q=";

		begin_search(initialUrl, query);
	}

	public static void begin_search(String initial_url, String query) throws Exception {
		Document doc = Jsoup.connect(initial_url + query).get();
		
		int score = 0;
		String title = doc.title();
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
}
