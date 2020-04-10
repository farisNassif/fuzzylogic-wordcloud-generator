package ie.gmit.sw.search.test;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import ie.gmit.sw.ai.cloud.WeightedFont;
import ie.gmit.sw.ai.cloud.WordFrequency;

public class BeamSearch implements Runnable {
	/* Wordcloud object containing query word, branching factor and max depth */
	private Wordcloud wordcloud;
	/* Contains URL's that were already visited */
	private Set<String> closed_list = new ConcurrentSkipListSet<>();
	private Queue<Node> queue = new PriorityQueue<>(Comparator.comparing(Node::getScore).reversed());
	private Map<String, Integer> word_freq = new ConcurrentHashMap<String, Integer>();

	public BeamSearch(Wordcloud wordcloud) {
		super();
		this.wordcloud = wordcloud;
	}

	@Override
	public void run() {
		/* Don't want the wordcloud to contain the query word */
		IgnoreWords.ignoreQuery(wordcloud.word);
		System.out.println("Processing wordcloud ...");

		/* Start processing */
		InitializeSearch();

		/* Generate freq table & sort word map highest > lowest */
		WordFrequency[] words = new WeightedFont().getFontSizes(GenerateFrequency(MapSort.crunchifySortMap(word_freq)));

		// Testing words are correct
		for (int s = 0; s < words.length; s++) {

			System.out.println(words[s]);
		}
		System.out.println("Finished");

	}

	private void InitializeSearch() {
		
	}
}
