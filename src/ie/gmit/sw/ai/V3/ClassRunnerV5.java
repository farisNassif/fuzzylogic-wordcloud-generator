package ie.gmit.sw.ai.V3;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ie.gmit.sw.ai.IgnoreWords;

public class ClassRunnerV5 implements Runnable {
	private final static int BRANCHING_FACTOR = 5;
	private static Document_Weights html_weights = new Document_Weights();
	private static Set<String> closed_list = new ConcurrentSkipListSet<>();
	// .reversed() makes life a lot easier when polling the queue, actually polls
	// highest valued first
	private Queue<UrlNode> queue = new PriorityQueue<>(Comparator.comparing(UrlNode::getScore).reversed());
	public static String query_text = "galway";

	@Override
	public void run() {
		String initial_url = "https://duckduckgo.com/html/?q=";
		int counter = 0;
		Document doc = null;

		try {
			doc = Jsoup.connect(initial_url + query_text).get();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		closed_list.add(initial_url);
		System.out.println("ClosedSize: " + closed_list.size() + " " + initial_url + "\n");

		try {
			genEdges(doc);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		while (!queue.isEmpty()) {
			System.out.println(queue.peek().getUrl() + " " + queue.peek().getScore());
			try {
				generateChildren(queue.poll().getUrl());
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	public void genEdges(Document doc) throws Throwable {
		Elements edges = doc.select("a[href]");
		int counter = 0;
		for (Element e : edges) {
			String link = e.absUrl("href");
			if (link != null && !closed_list.contains(link) && counter < BRANCHING_FACTOR && !link.contains("duck")) {
				scoreUrlContents(link);

				closed_list.add(link);
				System.out.println("ClosedSize: " + closed_list.size() + " " + link);
				counter++;
			}
		}
	}

	//
	public void scoreUrlContents(String url_to_score) throws Throwable {
		// Random r = new Random();
		// int randomInt = r.nextInt(100) + 1;
		Collection<String> ignoreWords = IgnoreWords.ignoreWords();
		int heuristicScore = 0;
		
		Document doc = Jsoup.connect(url_to_score).get();
		String h1_text = doc.select("h1").nextAll().text();
		String meta_text = doc.select("h1").nextAll().text();
		
		String[] link_data = h1_text.split(" ");
		for (String d : link_data) {
			if (d.contains(query_text)) {
				heuristicScore+=20;
			}
		}
		
		link_data = meta_text.split(" ");
		for (String d : link_data) {
			if (d.contains(query_text)) {
				heuristicScore+=25;
			}
		}
		
		queue.offer(new UrlNode(url_to_score, heuristicScore));
	}

	// Generate next gen children based on highest heuristically scoring child
	public void generateChildren(String child_to_expand) throws Throwable {
		System.out.println("POLLED " + child_to_expand);
		Document doc = Jsoup.connect(child_to_expand).get();

		genEdges(doc);
	}
}
