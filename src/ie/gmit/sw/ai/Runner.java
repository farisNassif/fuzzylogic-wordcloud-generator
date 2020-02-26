package ie.gmit.sw.ai;

import java.awt.RenderingHints.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Runner {

	public static void main(String[] args) throws Throwable {
		Map<String,Integer> word_and_frequency = new TreeMap<String,Integer>();
		
		int freq = 1;
		Collection<String> ignoreWords = IgnoreWords.ignoreWords();
		
		String query = "galway mayo institute of technology";
		Document doc = Jsoup.connect("https://duckduckgo.com/html/?q=" + query)
		.userAgent("Mozilla").get();
		Elements res = doc.getElementById("links").getElementsByClass("results_links");

		for (Element r : res) {
			Element title = r.getElementsByClass("links_main").first().getElementsByTag("a").first();
			System.out.println("URL:\t" + title.attr("href"));
			//System.out.println("Title:\t" + title.text());
			String text = r.getElementsByClass("result__snippet").first().wholeText();
			
			text = text.replaceAll("[^a-zA-Z]", " ");
			text = text.toLowerCase();
			String[] words = text.split(" ");
			for (String s : words) {
				if (ignoreWords.contains(s)) {
					System.out.println(s + " IGNORED");
				} else if (s.length() <= 2) {
					System.out.println(s + "  TOO SMALL");
				} else if (word_and_frequency.containsKey(s)) {
					word_and_frequency.replace(s, word_and_frequency.get(s), word_and_frequency.get(s) + 1);
				} else if (query.contains(s)) {
					
				} else {
					word_and_frequency.put(s, freq);
				}
				
				//wf.setWord(s);
				//System.out.println("WORD: "+s);
			}
			
			//System.out.println("Text:\t" + r.getElementsByClass("result__snippet").first().wholeText());
		}
		
		word_and_frequency.remove("Criminal");
		word_and_frequency.remove(query);
		word_and_frequency.values().stream().sorted().forEach(System.out::println); // Print the highest frequency in ascending order (ONLY FREQUENCY VALUE)
		
		System.out.println(word_and_frequency); // Print the elements in the tree and their values 
		//String key = Collections.max(word_and_frequency.entrySet(), Map.Entry.comparingByValue()).getKey();
		//System.out.println(key);
		
	}
}
