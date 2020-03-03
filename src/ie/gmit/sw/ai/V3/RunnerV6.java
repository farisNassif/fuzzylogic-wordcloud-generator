package ie.gmit.sw.ai.V3;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ie.gmit.sw.ai.V3.Document_Weights.Weight_Names;

public class RunnerV6 {
	public static Set<String> closed_list = new ConcurrentSkipListSet<>();
	// .reversed() makes life a lot easier when polling the queue, actually polls
	// highest valued first
	public static Queue<UrlNode> queue = new PriorityQueue<>(Comparator.comparing(UrlNode::getScore).reversed());
	public static Collection<Weight_Names> weight_keyset = Document_Weights.Weight_Value_Mapping().keySet();

	final static int BRANCHING_FACTOR = 5;
	final static int MAX_URLS_TO_VISIT = 30;
	public static String query = "software";
	public static int i = 0;
	public static int counter = 0;
	public static int heuristicScore = 0;
	public static Document doc = null;

	public static void main(String[] args) throws Throwable {

		String initial_url = "https://duckduckgo.com/html/?q=";

		doc = Jsoup.connect(initial_url + query).get();
		closed_list.add(initial_url);

		Elements elements = doc.select("a"); // Get all the links
		genEdges(elements); // TODO
	}

	public static void genEdges(Elements elements) throws Throwable {
		// For each link on the page ..
		for (Element e : elements) {
			// Save the specific link as this variable
			String link = e.attr("href"); 

			if (!closed_list.contains(link) && link != null && link.contains("https:")) { // As long as conditions are met
				closed_list.add(link); // Link is good, add to closed so don't see it again
				scoreUrlContents(link); // Score the content of the link
			}
		}
	}

	public static int scoreUrlContents(String url_to_score) throws Throwable {
		i = 0;
		heuristicScore = 0;
		Document doc = Jsoup.connect(url_to_score).get();
		// Check URL for 6 different tags/scores
		for (Object s : weight_keyset) {
			String text_to_extract = "";
			if (Document_Weights.Weight_Name_Array()[i].contains("h")) {
				// Deals with all H[n] tags
				text_to_extract = doc.select(Document_Weights.Weight_Name_Array()[i]).nextAll().text();
			} else if (Document_Weights.Weight_Name_Array()[i].contains("body")
					|| Document_Weights.Weight_Name_Array()[i].contains("title")) {
				// Deals with all Options not including H[n] tags or meta tags
				text_to_extract = doc.select(Document_Weights.Weight_Name_Array()[i]).text();
			} else {
				/*
				 * Finally dealing with meta tags, bit more difficult since need to make if of
				 * type Elements, some pre-pre processing involved
				 */
				Elements meta_text = doc.getElementsByTag(Document_Weights.Weight_Name_Array()[i]);
				for (Element metaTag : meta_text) {
					String content = metaTag.attr("content");
					String name = metaTag.attr("name");
					content += name;
					text_to_extract = content;
				}
			}

			String[] tag_data = text_to_extract.split(" ");
			for (String d : tag_data) {
				if (d.contains(query)) {
					heuristicScore += Document_Weights.Weight_Value_Mapping().get(s);
					//System.out.println(heuristicScore + " Added: " + Document_Weights.Weight_Value_Mapping().get(s)
					//		+ " " + Document_Weights.Weight_Name_Array()[i]);
				}
			}

			// Print out if I wanna check individual url scorings

			// System.out.println(Document_Weights.Weight_Value_Mapping().get(s));
			i++;
		}
		System.out.println(url_to_score + " " + heuristicScore);
		queue.offer(new UrlNode(url_to_score, heuristicScore));
		return heuristicScore;
	}

	// Generate next gen children based on highest heuristically scoring child
	public static void generateChildren(String child_to_expand) throws Throwable {
		System.out.println("POLLED " + child_to_expand);
		Document doc = Jsoup.connect(child_to_expand).get();
		// genEdges(doc);
	}
}
