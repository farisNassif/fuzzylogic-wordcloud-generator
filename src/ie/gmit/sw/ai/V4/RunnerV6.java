package ie.gmit.sw.ai.V4;

import java.util.Collection;
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

import ie.gmit.sw.ai.WeightedFont;
import ie.gmit.sw.ai.WordFrequency;
import ie.gmit.sw.ai.V4.Document_Weights.Weight_Names;

public class RunnerV6 {
	public static Set<String> closed_list = new ConcurrentSkipListSet<>();
	// .reversed() makes life a lot easier when polling the queue, actually polls
	// highest valued first
	public static Queue<UrlNode> queue = new PriorityQueue<>(Comparator.comparing(UrlNode::getScore).reversed());
	public static Collection<Weight_Names> weight_keyset = Document_Weights.Weight_Value_Mapping().keySet();
	public static Map<String, Integer> word_freq = new ConcurrentHashMap<String, Integer>();
	
	public static boolean EMERGENCY_STOP = false;
	final static int BRANCHING_FACTOR = 2;
	final static int MAX_URLS_TO_VISIT = 10;
	public static String query = "computer";
	public static int i = 0;
	public static int counter = 0;
	public static int heuristicScore = 0;
	public static Document doc = null;

	public static void main(String[] args) throws Throwable {

		String initial_url = "https://duckduckgo.com/html/?q=";

		doc = Jsoup.connect(initial_url + query).get();
		closed_list.add(initial_url);

		Elements elements = doc.select("a"); // Get all the links
		generateChildren(elements); // TODO

		word_freq = MapSort.crunchifySortMap(word_freq); // Sort the map in reverse order

		int[] freqs = new int[word_freq.size()];
		String[] strings = new String[word_freq.size()];

		for (int i = 0; i < word_freq.size(); i++) {
			freqs[i] = (int) word_freq.values().toArray()[i];
			strings[i] = (String) word_freq.keySet().toArray()[i];
		}

		System.out.println(word_freq);
		WordFrequency[] words = new WeightedFont().getFontSizes(getWordFrequencyKeyValue(32, freqs, strings));

		// Testing words are correct
		for (int s = 0; s < words.length; s++) {
			System.out.println(words[s]);
		}
	}

	public static void generateChildren(Elements elements) {
		int birthControl = 0;
		// For each link on the page ..
		for (Element e : elements) {

			// Save the specific link as this variable
			String link = e.attr("href");

			if (!closed_list.contains(link) && link != null && link.contains("https:")
					&& birthControl != BRANCHING_FACTOR && EMERGENCY_STOP != true
					&& MAX_URLS_TO_VISIT > closed_list.size()) { // As long as conditions are met
				birthControl++;
				closed_list.add(link); // Link is good, add to closed so don't see it again
				try {
					scoreUrlContents(link); // Score the content of the link
				} catch (Throwable e1) {
					e1.printStackTrace();
				}
			}
		}
		if (MAX_URLS_TO_VISIT > closed_list.size() && EMERGENCY_STOP != true)
			try {
				generateGrandchildren(queue.peek().getUrl());
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
	}

	public static void generateGrandchildren(String url_to_expand) throws Throwable {
		System.out.println(" ==> Expanding on " + url_to_expand + " - [Heuristic Value]: " + queue.peek().getScore());
		if (queue.peek().getScore() == 0) {
			EMERGENCY_STOP = true;
			System.out.println("Stopped the search as highest heuristic was 0");
		}
		queue.poll();
		doc = Jsoup.connect(url_to_expand).get();
		String Wholetext = doc.wholeText();
		mapWords(Wholetext);
		Elements elements = doc.select("a"); // Get all the links
		generateChildren(elements); // TOD
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
					// System.out.println(heuristicScore + " Added: " +
					// Document_Weights.Weight_Value_Mapping().get(s)
					// + " " + Document_Weights.Weight_Name_Array()[i]);
				}
			}
			i++;
		}
		System.out.println(url_to_score + ": Heuristic Score => " + heuristicScore);
		queue.offer(new UrlNode(url_to_score, heuristicScore));
		return heuristicScore;
	}

	public static void mapWords(String wholeText) throws Throwable {
		wholeText = wholeText.replaceAll("[^a-zA-Z]", " ");
		wholeText = wholeText.toLowerCase();
		String[] words = wholeText.split(" ");

		for (String s : words) {
			if (IgnoreWords.ignoreWords().contains(s)) {
				// System.out.println(s + " IGNORED");
			} else if (s.length() <= 2) {
				// System.out.println(s + " TOO SMALL");
			} else if (word_freq.containsKey(s)) {
				word_freq.replace(s, word_freq.get(s), word_freq.get(s) + 1);
			} else if (query.contains(s)) {

			} else {
				word_freq.put(s, 1);
			}
		}

	}

	private static WordFrequency[] getWordFrequencyKeyValue(int amt_words_for_cloud, int[] freq, String[] values) {
		WordFrequency[] wf = new WordFrequency[amt_words_for_cloud];

		for (int i = 0; i < amt_words_for_cloud; i++) {
			wf[i] = new WordFrequency(values[i], freq[i]);
		}

		return wf;
	}

	// Generate next gen children based on highest heuristically scoring child
	public static void generateGrandchildrens(String child_to_expand) throws Throwable {
		System.out.println("POLLED " + child_to_expand);
		Document doc = Jsoup.connect(child_to_expand).get();
		// genEdges(doc);
	}
}
