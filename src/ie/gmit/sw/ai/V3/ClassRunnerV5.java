package ie.gmit.sw.ai.V3;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ie.gmit.sw.ai.V4.Document_Weights;
import ie.gmit.sw.ai.V4.IgnoreWords;
import ie.gmit.sw.ai.V4.UrlScore;

public class ClassRunnerV5 implements Runnable {
	private final static int BRANCHING_FACTOR = 5;
	private final static int MAX_URLS_TO_VISIT = 20;
	private static Document_Weights html_weights = new Document_Weights();
	private static Set<String> closed_list = new ConcurrentSkipListSet<>();
	// .reversed() makes life a lot easier when polling the queue, actually polls
	// highest valued first
	private Queue<UrlScore> queue = new PriorityQueue<>(Comparator.comparing(UrlScore::getScore).reversed());
	public static String query_text = "add";

	@Override
	public void run() {
		String initial_url = "https://duckduckgo.com/html/?q=";
		int counter = 0;
		Document doc = null;

		try {
			doc = Jsoup.connect(initial_url + query_text).ignoreContentType(true).get();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		closed_list.add(initial_url);
		// System.out.println("ClosedSize: " + closed_list.size() + " " + initial_url +
		// "\n");

		try {
			genEdges(doc);
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	public void genEdges(Document doc) throws Throwable {
		Elements edges = doc.select("a[href]");

		int counter = 0;
		for (Element e : edges) {
			String link = e.attr("abs:href");
			if (link != null && !closed_list.contains(link) && closed_list.size() <= MAX_URLS_TO_VISIT
					&& counter <= BRANCHING_FACTOR && !closed_list.contains(getBaseUrl(link))) {
				scoreUrlContents(link);

				closed_list.add(getBaseUrl(link));
				// System.out.println("ClosedSize: " + closed_list.size() + " " + link);
				counter++;
			}
		}
		while (!queue.isEmpty()) {
			System.out.println(queue.peek().getUrl() + " " + queue.peek().getScore());
			try {
				String make_children_url = queue.poll().getUrl();
				generateChildren(make_children_url);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	//
	public int scoreUrlContents(String url_to_score) throws Throwable {
		Random r = new Random(); // Coinflipping preventing drunkards walk
		int randomInt = r.nextInt(100) + 1;
		Collection<String> ignoreWords = IgnoreWords.ignoreWords();
		int heuristicScore = 0;

		Document doc = Jsoup.connect(url_to_score).get();
		String h1_text = doc.select("h1").nextAll().text();
		String h2_text = doc.select("h2").nextAll().text();
		String h3_text = doc.select("h3").nextAll().text();
		String body_text = doc.body().text();
		Elements meta_text = doc.getElementsByTag("meta");

		if (url_to_score.contains(query_text)) {
			heuristicScore += 45;
		}

		String[] link_data = h1_text.split(" ");
		for (String d : link_data) {
			if (d.contains(query_text)) {
				heuristicScore += 20;
			}
		}

		link_data = h2_text.split(" ");
		for (String d : link_data) {
			if (d.contains(query_text)) {
				heuristicScore += 17;
			}
		}

		link_data = h3_text.split(" ");
		for (String d : link_data) {
			if (d.contains(query_text)) {
				heuristicScore += 15;
			}
		}

		link_data = body_text.split(" ");
		for (String d : link_data) {
			if (d.contains(query_text)) {
				heuristicScore += 10;
			}
		}

		for (Element metaTag : meta_text) {
			String content = metaTag.attr("content");
			String name = metaTag.attr("name");
			content += name;
			String[] meta_content = content.split(" ");
			for (String f : meta_content) {
				if (f.contains(query_text)) {
					heuristicScore += 25;
				}
			}
		}

		if (randomInt >= 50) {
			heuristicScore += (heuristicScore * 0.17); // Adding a bit of randomness to prevent plateauing
		} else {
			// queue.poll();
			heuristicScore -= (heuristicScore * 0.05); // Adding a bit of randomness to prevent plateauing
		}

		// Print out if I wanna check individual url scorings
		System.out.println(url_to_score + " " + heuristicScore);
		queue.offer(new UrlScore(url_to_score, heuristicScore));

		return heuristicScore;
	}

	// Generate next gen children based on highest heuristically scoring child
	public void generateChildren(String child_to_expand) throws Throwable {
		System.out.println("POLLED " + child_to_expand);
		Document doc = Jsoup.connect(child_to_expand).get();
		genEdges(doc);
	}

	public String getBaseUrl(String absUrl) {
		String baseUrl = null;
		try {
			baseUrl = absUrl.substring(0, absUrl.indexOf('/', absUrl.indexOf("//") + 2));
			// System.out.println(baseUrl + "<== Base");
		} catch (Exception e) {
			System.out.println(e);
		}
		return baseUrl;
	}
}
