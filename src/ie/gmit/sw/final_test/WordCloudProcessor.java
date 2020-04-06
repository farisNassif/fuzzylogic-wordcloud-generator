package ie.gmit.sw.final_test;

import java.io.IOException;
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

import ie.gmit.sw.final_test.utilities.MapSort;

/* Handles the internal processing of the wordcloud */
public class WordCloudProcessor implements Runnable {
	/* Wordcloud object containing query word, branching factor and max depth */
	private Wordcloud wordcloud;
	/* Contains URL's that were already visited */
	private Set<String> closed_list = new ConcurrentSkipListSet<>();
	/* Maps a word to it's frequency */
	private Map<String, Integer> word_freq = new ConcurrentHashMap<String, Integer>();
	/* Maps words preceeding and proceeding the query word to frequency */
	private Map<String, Integer> associated_word_freq = new ConcurrentHashMap<String, Integer>();
	/* Holds all scored URL's */
	private Queue<UrlNode> queue = new PriorityQueue<>(Comparator.comparing(UrlNode::getScore).reversed());
	private boolean RECURSIVELY_CALL = true;

	public WordCloudProcessor(Wordcloud wordcloud) {
		super();
		this.wordcloud = wordcloud;
	}

	@Override
	public void run() {
		System.out.println("Processing wordcloud ...");
		/* Start processing */
		InitializeSearch();
		System.out.println("Finished");
	}

	/* Kicks off the search */
	private void InitializeSearch() {
		String initial_url = "https://duckduckgo.com/html/?q=";

		/* Kick off URL search and add initial URL to the closed list */
		Document doc = null;
		try {
			doc = Jsoup.connect(initial_url + wordcloud.word).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		closed_list.add(initial_url);

		/* Get the resulting links of the initial URL and query text */
		Elements elements = doc.select("a");

		/* Generate child URL nodes based on initial query */
		GenerateChildNodes(elements);
	}

	/* Gen nodes based on initial search, discards pointless URLs or visited URLs */
	private void GenerateChildNodes(Elements elements) {
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

				/* Pass the URL to be scored */
				try {
					ScoreChildren(link);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		/* If conditions haven't been met to stop recursive calls .. */
		if (RECURSIVELY_CALL) {
			System.out.println("HIGHEST SCORING URL => " + queue.peek().getUrl());
			System.out.println("Generating new URLS From highest scoring URL");

			Document doc = null;
			/* Start focusing on URL's on the highest scoring page */
			try {
				doc = Jsoup.connect(queue.poll().getUrl()).get();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Elements child_elements = doc.select("a");

			/* Recursive call to this method */
			GenerateChildNodes(child_elements);
		}
	}

	/* Takes a child URL and will score it based on relevance */
	private void ScoreChildren(String child) throws IOException {
		int tempHeuristic = 0;

		/* Connec to child URL */
		Document doc = Jsoup.connect(child).get();

		/* Get the body text of the URL without numbers and symbols */
		String textToExtract = doc.select("body").text().replaceAll("[^a-zA-Z]+", " ");

		/* Split the contents of the extracted text with a space and put into array */
		String[] tag_data = textToExtract.split(" ");

		/* For each word in the array .. */
		for (String word : tag_data) {
			/* If the queried word is found .. */
			if (word.toLowerCase().contains(wordcloud.word.toLowerCase())) {
				/* Heuristic score for this URL should increase */
				tempHeuristic++;
			}
		}
		System.out.println(child + ": Heuristic Score => " + tempHeuristic);
		/* Map this URL and it's heuristic score, shove it onto priority queue */
		queue.offer(new UrlNode(child, tempHeuristic));
	}
}
