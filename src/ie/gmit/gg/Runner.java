package ie.gmit.gg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// Everything in this class is disgusting cowboy code
public class Runner {

	public static void main(String[] args) throws Throwable {
		TreeMap<String, Integer> word_and_frequency = new TreeMap<String, Integer>();
		Map<String, Integer> sorted_words = new HashMap<String, Integer>();
		Collection<String> ignoreWords = IgnoreWords.ignoreWords();
		List<String> urls = new ArrayList<String>();
		Document doc;
		Document doca;
		Elements res;
		String text;
		int freq = 1;

		String query = "hitler";
		doc = Jsoup.connect("https://duckduckgo.com/html/?q=" + query).get();
		res = doc.getElementById("links").getElementsByClass("results_links");

		for (Element r : res) {
			Element title = r.getElementsByClass("links_main").first().getElementsByTag("a").first();
			text = r.getElementsByClass("result__snippet").first().wholeText();
			
			urls.add(title.attr("href"));
			
			text = text.replaceAll("[^a-zA-Z]", " ");
			text = text.toLowerCase();
			String[] words = text.split(" ");

			for (String s : words) {
				if (ignoreWords.contains(s)) {
					// System.out.println(s + " IGNORED");
				} else if (s.length() <= 2) {
					// System.out.println(s + " TOO SMALL");
				} else if (word_and_frequency.containsKey(s)) {
					word_and_frequency.replace(s, word_and_frequency.get(s), word_and_frequency.get(s) + 1);
				} else if (query.contains(s)) {

				} else {
					word_and_frequency.put(s, freq);
				}
			}
		}
		
		
		int ctrl = 0;
		for (String url : urls) {
			if (ctrl < 5) {
				doca = Jsoup.connect(url).get();
				text = doca.select("h2").nextAll().text();
				
				text = text.replaceAll("[^a-zA-Z]", " ");
				text = text.toLowerCase();
				
				String[] words = text.split(" ");
				
				for (String s : words) {
					if (ignoreWords.contains(s)) {
						// System.out.println(s + " IGNORED");
					} else if (s.length() <= 2) {
						// System.out.println(s + " TOO SMALL");
					} else if (word_and_frequency.containsKey(s)) {
						word_and_frequency.replace(s, word_and_frequency.get(s), word_and_frequency.get(s) + 1);
					} else if (query.contains(s)) {

					} else {
						word_and_frequency.put(s, freq);
					}
				}
				ctrl ++;
			}
		}
		
		sorted_words = sortByValue(word_and_frequency);

		int[] freqs = new int[sorted_words.size()];
		String[] strings = new String[sorted_words.size()];

		for (int i = 0; i < sorted_words.size(); i++) {
			freqs[i] = (int) sorted_words.values().toArray()[i];
			strings[i] = (String) sorted_words.keySet().toArray()[i];
		}

		WordFrequency[] words = new WeightedFont()
				.getFontSizes(getWordFrequencyKeyValue(32, freqs, strings, sorted_words.size()));

		// Testing words are correct
		for (int s = 0; s < words.length; s++) {
			System.out.println(words[s]);
		}
	}

	private static WordFrequency[] getWordFrequencyKeyValue(int amt_words_for_cloud, int[] freq, String[] values, int idontknowthefuckimdoing) {
		WordFrequency[] wf = new WordFrequency[amt_words_for_cloud];

		for (int i = idontknowthefuckimdoing; i >= (idontknowthefuckimdoing - (amt_words_for_cloud - 1)); i--) {
			wf[idontknowthefuckimdoing - i] = new WordFrequency(values[i - 1], freq[i - 1]);
		}
		
		return wf;
	}

	public static <Q, Z extends Comparable<? super Integer>> Map<String, Integer> sortByValue(Map<String, Integer> map) {
		List<Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
		list.sort(Entry.comparingByValue());

		Map<String, Integer> sorted_map = new LinkedHashMap<>();

		for (Entry<String, Integer> entry : list) {
			sorted_map.put(entry.getKey(), entry.getValue());
		}
		
		return sorted_map;
	}
}
