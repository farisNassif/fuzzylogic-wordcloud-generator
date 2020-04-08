package complete.deploy;

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

/* Handles the internal processing of the wordcloud */
public class WordCloudProcessor implements Runnable {
	/* Wordcloud object containing query word, branching factor and max depth */
	private Wordcloud wordcloud;
	/* Contains URL's that were already visited */
	private Set<String> closed_list = new ConcurrentSkipListSet<>();
	private Queue<Node> queue = new PriorityQueue<>(Comparator.comparing(Node::getScore).reversed());
	private Map<String, Integer> word_freq = new ConcurrentHashMap<String, Integer>();

	public WordCloudProcessor(Wordcloud wordcloud) {
		super();
		this.wordcloud = wordcloud;
	}

	@Override
	public void run() {
		IgnoreWords.ignoreQuery(wordcloud.word);
		System.out.println("Processing wordcloud ...");

		/* Start processing */
		InitializeSearch();

		word_freq = MapSort.crunchifySortMap(word_freq);
		// adj_word_freq = MapSort.crunchifySortMap(adj_word_freq);

		System.out.println(word_freq.entrySet());

		System.out.println("Finished");
	}

	/* Kicks off the search */
	private void InitializeSearch() {
		String queryUrl = "https://duckduckgo.com/html/?q=" + wordcloud.word;

		Node initial = new Node(queryUrl, 0);

		GenerateChildren(initial);
	}

	private void GenerateChildren(Node parent) {
		int count = 0;

		Document doc = null;
		try {
			doc = Jsoup.connect(parent.getUrl()).get();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Elements children = doc.select("a");

		for (Element child : children) {
			String link = child.attr("href");

			if (!closed_list.contains(link) && link.contains("https://") && count < wordcloud.brachingFactor) {

				Node childNode = new Node(link, parent.getDepth() + 1);

				closed_list.add(link);

				ScoreChild(childNode);
				count++;
			}
		}

		if (closed_list.size() < 10) {
			MapWords(queue.peek());
			GenerateChildren(queue.poll());
		}
	}

	/* Takes a child URL and will score it based on relevance */
	private void ScoreChild(Node child) {
		/* Connect to child URL */
		Document doc = null;

		try {
			doc = Jsoup.connect(child.getUrl()).get();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/* Get Title data without numbers and symbols */
		String title = doc.title().replaceAll("[^a-zA-Z]+", " ").toLowerCase();

		/* Get Heading data without numbers and symbols */
		String headings = doc.select("h1,h2,h3").text().replaceAll("[^a-zA-Z]+", " ").toLowerCase();

		/* Get Paragraph data without numbers and symbols */
		String paragraph = doc.select("p").text().replaceAll("[^a-zA-Z]+", " ").toLowerCase();

		// System.out.println(child.getUrl() + ": Heuristic Score => "
		// + RelevanceCalculator.UrlRelevance(child, title, headings, paragraph,
		// wordcloud.word) + " Depth: "
		// + child.getDepth());

		child.setScore(RelevanceCalculator.UrlRelevance(child, title, headings, paragraph, wordcloud.word));
		/* Map this URL and it's heuristic score, shove it onto priority queue */
		queue.offer(child);
	}

	/* Maps all words on the highest scoring page to frequency */
	private void MapWords(Node bestChild) {
		Document doc = null;

		try {
			/* Connect to the highest */
			doc = Jsoup.connect(bestChild.getUrl()).get();
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
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
