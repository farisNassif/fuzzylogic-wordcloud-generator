package ie.gmit.sw.ai.V3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ClassRunnerV5 implements Runnable {
	private final static int BRANCHING_FACTOR = 5;
	private static Set<String> closed_list = new ConcurrentSkipListSet<>();

	// .reversed() makes life a lot easier when polling the queue, actually polls
	// highest valued first
	private Queue<UrlNode> queue = new PriorityQueue<>(Comparator.comparing(UrlNode::getScore).reversed());

	@Override
	public void run() {
		String initial_url = "https://duckduckgo.com/html/?q=";
		String query = "software";
		int counter = 0;
		Document doc = null;

		try {
			doc = Jsoup.connect(initial_url + query).get();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		closed_list.add(initial_url);
		System.out.println("ClosedSize: " + closed_list.size() + " " + initial_url + "\n");
		// Google results for query
		Elements edges = doc.select("a[href]");

		for (Element e : edges) {
			String link = e.absUrl("href");
			if (link != null && !closed_list.contains(link) && counter < BRANCHING_FACTOR && !link.contains("duck")) {
				queue.offer(new UrlNode(link, 43 + link.length()));
				closed_list.add(link);
				System.out.println("ClosedSize: " + closed_list.size() + " " + link);
				counter++;
			}
		}

		while (!queue.isEmpty()) {
			// System.out.println(queue.poll().getUrl() + " " + queue.poll().getScore());
			System.out.println(queue.peek().getUrl() + " " + queue.peek().getScore());
			queue.poll();
		}

		// Elements res =
		// doc.getElementById("links").getElementsByClass("results_links");

		// for (Element r : res) {
		// Element title =
		// r.getElementsByClass("links_main").first().getElementsByTag("a").first();
		// // System.out.println(title.attr("href"));
		// // urls.add();
		// }

	}

	public void scoreUrl() throws Throwable {

	}

	public void processChildren() throws Throwable {

	}
}
