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

import ie.gmit.sw.ai.V4.IgnoreWords;
import ie.gmit.sw.final_test.utilities.MapSort;

/* Handles the internal processing of the wordcloud */
public class WordCloudProcessor implements Runnable {
	/* Wordcloud object containing query word, branching factor and max depth */
	private Wordcloud wordcloud;
	/* Contains URL's that were already visited */
	private Set<String> closed_list = new ConcurrentSkipListSet<>();
	/* Maps a word to it's frequency */
	private Map<String, Integer> word_freq = new ConcurrentHashMap<String, Integer>();
	/* Maps an adjacent word to it's frequency */
	private Map<String, Integer> adj_word_freq = new ConcurrentHashMap<String, Integer>();
	/* Holds all scored URL's */
	private Queue<UrlNode> queue = new PriorityQueue<>(Comparator.comparing(UrlNode::getScore).reversed());
	/* To check if conditions are met to continue recursive calls */
	private boolean RECURSIVELY_CALL = true;

	public WordCloudProcessor(Wordcloud wordcloud) {
		super();
		this.wordcloud = wordcloud;
	}

	@Override
	public void run() {
		/* Query word to be ignored when generating freq table */
		IgnoreWords.ignoreQuery(wordcloud.word);

		System.out.println("Processing wordcloud ...");
		/* Start processing */
		InitializeSearch();

		/* Sort the maps in reverse order high to low values */
		word_freq = MapSort.crunchifySortMap(word_freq);
		// adj_word_freq = MapSort.crunchifySortMap(adj_word_freq);

		System.out.println(word_freq.entrySet());
		// System.out.println(adj_word_freq.entrySet());
		System.out.println("Finished");
	}

	/* Kicks off the search */
	private void InitializeSearch() {
		String initial_url = "https://duckduckgo.com/html/?q=";
		Document doc = null;

		try {
			/* Kick off initial search for query word */
			doc = Jsoup.connect(initial_url + wordcloud.word).get();
			// doc = Jsoup.connect(initial_url + wordcloud.word).userAgent("Mozilla").get();
			/* Was visited, add to closed list */
			closed_list.add(initial_url);
		} catch (IOException e) {
			e.printStackTrace();
		}

		/* Get the resulting links of the initial URL and query text */
		Elements elements = doc.select("a");
		System.out.println(elements.parents().last());
		/* Generate child URL nodes based on initial query */
		GenerateChildNodes(elements);
	}

	/* Gen nodes based on initial search, discards pointless URLs or visited URLs */
	private void GenerateChildNodes(Elements children) {
		/* Variable to just control branching factor */
		int birthControl = 1;
		/* If conditions haven't been met to stop recursive calls .. */
		if (RECURSIVELY_CALL) {
			/* For each child url .. */
			for (Element child : children) {
				/* Get absolute URL and clean any HTML syntax off it */
				String link = child.attr("href");

				/* Making sure to only check links that are worthy and not pointless */
				if (!closed_list.contains(link) && link.contains("https://")
						&& birthControl <= wordcloud.brachingFactor) {
					/* Counter to control branching factor */
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

			System.out.println("HIGHEST SCORING URL => " + queue.peek().getUrl());
			System.out.println("Generating new URLS From highest scoring URL");

			Document doc = null;
			/* Start focusing on URL's on the highest scoring page */
			try {
				/* Assign to this the highest scoring url */
				doc = Jsoup.connect(queue.peek().getUrl()).get();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			/* Get rid of highest scoring URL, score its contents */
			MapWords(queue.poll().getUrl());

			/* Make new children links from the child with highest heuristic */
			Elements recursive_children = doc.select("a");

			/* If max amounts of URL's have been visited, stop recursive calls */
			if (closed_list.size() >= wordcloud.maxDepth) {
				RECURSIVELY_CALL = false;
			}

			/* Recursive call to this method */
			GenerateChildNodes(recursive_children);
		}
	}

	/* Takes a child URL and will score it based on relevance */
	private void ScoreChildren(String child) throws IOException {
		/* Connec to child URL */
		Document doc = Jsoup.connect(child).get();

		/* Get Title data without numbers and symbols */
		String title = doc.title().replaceAll("[^a-zA-Z]+", " ").toLowerCase();

		/* Get Heading data without numbers and symbols */
		String headings = doc.select("h1,h2,h3").text().replaceAll("[^a-zA-Z]+", " ").toLowerCase();

		/* Get Paragraph data without numbers and symbols */
		String paragraph = doc.select("p").text().replaceAll("[^a-zA-Z]+", " ").toLowerCase();

		System.out.println(child + ": Heuristic Score => "
				+ RelevanceCalculator.UrlRelevance(child, title, headings, paragraph, wordcloud.word.toLowerCase()));
		/* Map this URL and it's heuristic score, shove it onto priority queue */
		queue.offer(new UrlNode(child,
				RelevanceCalculator.UrlRelevance(child, title, headings, paragraph, wordcloud.word.toLowerCase())));
	}

	/* Maps all words on the highest scoring page to frequency */
	private void MapWords(String url) {
		Document doc = null;
		/* Counter to retrieve adjacent words */
		int i = 0;
		try {
			/* Connect to the highest */
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/* Get the whole text without symbolds or numbers */
		String wholetext = doc.wholeText().replaceAll("[^a-zA-Z]", " ").toLowerCase();
		String[] words = wholetext.split(" ");

		/* For each word .. */
		for (String word : words) {
			try {
				/* If it's not worthless and irrelevant .. */
				if ((word.length() > 2) && (!IgnoreWords.ignoreWords().contains(word))) {
					/* If the word was already mapped */
					if (word_freq.containsKey(word)) {
						/* If it was encountered before, increment */
						word_freq.replace(word, word_freq.get(word), word_freq.get(word) + 1);
					} else {
						/* If this was the first time encountering it, put into map */
						word_freq.put(word, 1);
					}
				}

				/* Start taking note of words adjacent the query word .. */
				/*
				 * if (word.contains(wordcloud.word.toLowerCase()) && i > 3) { // Deal with
				 * predecessing word if (!IgnoreWords.ignoreWords().contains(words[i - 1]) &&
				 * words[i - 1].length() >= 2) { if (adj_word_freq.containsKey(words[i - 1])) {
				 * // Increment this word in the adjacent map adj_word_freq.replace(words[i -
				 * 1], adj_word_freq.get(words[i - 1]), adj_word_freq.get(words[i - 1]) + 1); }
				 * else { adj_word_freq.put(words[i - 1], 1); } } Deal with succeeding word if
				 * (!IgnoreWords.ignoreWords().contains(words[i + 1]) && words[i + 1].length()
				 * >= 2) { if (adj_word_freq.containsKey(words[i + 1])) { // Increment this word
				 * in the adjacent map adj_word_freq.replace(words[i + 1],
				 * adj_word_freq.get(words[i + 1]), adj_word_freq.get(words[i + 1]) + 1); } else
				 * { adj_word_freq.put(words[i + 1], 1); } }
				 */
			} catch (IOException e) {
				e.printStackTrace();
			}
			i++;
		}
	}
}
