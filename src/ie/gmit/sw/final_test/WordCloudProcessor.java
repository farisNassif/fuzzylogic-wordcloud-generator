package ie.gmit.sw.final_test;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ie.gmit.sw.ai.V4.Document_Weights;
import ie.gmit.sw.ai.V4.UrlNode;

/* Handles the internal processing of the wordcloud */
public class WordCloudProcessor implements Runnable {
	/* Wordcloud object containing query word, branching factor and max depth */
	private Wordcloud wordcloud;
	/* Contains URL's that were already visited */
	private static Set<String> closed_list = new ConcurrentSkipListSet<>();
	/* Maps a word to it's frequency */
	public static Map<String, Integer> word_freq = new ConcurrentHashMap<String, Integer>();
	/* Holds all scored URL's */
	public static Queue<UrlNode> queue = new PriorityQueue<>(Comparator.comparing(UrlNode::getScore).reversed());
	
	public WordCloudProcessor(Wordcloud wordcloud) {
		super();
		this.wordcloud = wordcloud;
	}

	@Override
	public void run() {
		System.out.println("Processing wordcloud ...");
		try {
			/* Start processing */
			InitializeSearch();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finished");
	}

	/* Kicks off the search */
	public void InitializeSearch() throws IOException {
		String initial_url = "https://duckduckgo.com/html/?q=";

		/* Kick off URL search and add initial URL to the closed list */
		Document doc = Jsoup.connect(initial_url + wordcloud.word).get();
		closed_list.add(initial_url);

		/* Get the resulting links of the initial URL and query text */
		Elements elements = doc.select("a");

		/* Generate child URL nodes based on initial query */
		GenerateChildNodes(elements);
	}

	/* Gen nodes based on initial search, discards pointless URLs or visited URLs */
	public void GenerateChildNodes(Elements elements) throws IOException {
		/* Variable to just control branching factor */
		int birthControl = 1;
		for (Element e : elements) {
			/* Get absolute URL and clean any HTML syntax off it */
			String link = e.attr("href");

			/* Making sure to only check links that are worthy and not pointless */
			if (!closed_list.contains(link) && link.contains("https://") && birthControl <= wordcloud.brachingFactor) {
				/* Once this statement executes n times, stop */
				birthControl++;
				/* Was just visited, add to closed list to make sure don't visit it again */
				closed_list.add(link);
				System.out.println(link);

				/* Pass the URL to be scored */
				ScoreChildren(link);
			}
		}
	}

	/* Takes a child URL and will score it based on relevance */
	public void ScoreChildren(String child) throws IOException {
		// meta, title, h1, h2, h3, body
		Document doc = Jsoup.connect(child).get();
		String textToExtract = doc.select("body").text().replaceAll("[^a-zA-Z]+", " ");
		String[] tag_data = textToExtract.split(" ");
		
		for (String d : tag_data) {
			if(d.length() > 2 ) {
				System.out.println(d.toLowerCase());
			}
		}
	}
}
