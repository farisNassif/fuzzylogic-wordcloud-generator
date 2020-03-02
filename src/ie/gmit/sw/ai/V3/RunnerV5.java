package ie.gmit.sw.ai.V3;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RunnerV5 {
	private Queue<DocumentNode> queue = new PriorityQueue<>();
	private final static int BRANCHING_FACTOR = 5;
	private static Set<String> closed_list = new ConcurrentSkipListSet<>();

	public static void main(String[] args) throws Throwable {
		String query = "software";
		int counter = 0;
		Document doc = Jsoup.connect("https://duckduckgo.com/html/?q=" + query).userAgent("Mozilla").get();
		Elements res = doc.getElementById("links").getElementsByClass("results_links");

		// Google results for query
		Elements edges = doc.select("a[href]");

		for (Element e : edges) {

			String link = e.absUrl("href");

			if (link != null && !closed_list.contains(link) && counter <= BRANCHING_FACTOR) 
			{
				closed_list.add(link);
				System.out.println(link);
				counter++;
			}
			
		}

		for (Element r : res) {
			Element title = r.getElementsByClass("links_main").first().getElementsByTag("a").first();
			// System.out.println(title.attr("href"));
			// urls.add();

		}
	}
}
