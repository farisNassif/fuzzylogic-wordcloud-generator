package ancestor.attempt;

import java.io.IOException;
import java.util.ArrayList;
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

import ie.gmit.bbb.UrlNode;

public class Runner {
	private static Set<String> closed_list = new ConcurrentSkipListSet<>();
	private static Map<String, Set<String>> parents = new ConcurrentHashMap<String, Set<String>>();
	private static Queue<UrlNode> queue = new PriorityQueue<>(Comparator.comparing(UrlNode::getScore).reversed());
	public static Set<String> child_nodes = new ConcurrentSkipListSet<>();

	public static void main(String[] args) {
		InitializeSearch();
	}

	/* Kicks off the search */
	public static void InitializeSearch() {
		String query_url = "https://duckduckgo.com/html/?q=computer";
		Document doc = null;

		try {
			/* Kick off initial search for query word */
			doc = Jsoup.connect(query_url).get();
			// doc = Jsoup.connect(initial_url + "bla").userAgent("Mozilla").get();
			/* Was visited, add to closed list */
			closed_list.add(query_url);
		} catch (IOException e) {
			e.printStackTrace();
		}

		/* Get the resulting links of the initial URL and query text */
		Elements elements = doc.select("a");
		for (Element child : elements) {
			String link = child.attr("href");
			if (!closed_list.contains(link) && link.contains("https://")) {
				closed_list.add(link);
				child_nodes.add(link);
				// score(child);
			}
		}

		parents.put(query_url, child_nodes);

		/* Generate child URL nodes based on initial query */
		GenerateChildNodes(elements);
	}

	public static void GenerateChildNodes(Elements children) {
		Set<String> more_children = new ConcurrentSkipListSet<>();

		for (Element child : children) {
			String link = child.attr("href");

			if (!closed_list.contains(link) && link.contains("https://")) {
				closed_list.add(link);
				more_children.add(link);
				score(child);
			}
		}
		System.out.println(parents.entrySet());
	}

	private static void score(Element child) {
		int max = 1000;
		int min = 1;
		int range = max - min + 1;
		int rand = (int) (Math.random() * range) + min;

		queue.offer(new UrlNode(child.attr("href"), rand));
	}
}
