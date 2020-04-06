package ie.gmit.sw.final_test;

import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Queue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/* Initially had the contents of this class in WordCloudProcessor, stuck them in here to clean it up a bit */
public class Score {

	/* Takes a child URL and will score it based on relevance */
	public static void ScoreChildren(String child, Queue<UrlNode> queue) throws IOException {
		int tempHeuristic = 0;
		int i = 0;

		/* Connec to child URL */
		Document doc = Jsoup.connect(child).get();

		/* Get the body text of the URL without numbers and symbols */
		String textToExtract = doc.select("body").text().replaceAll("[^a-zA-Z]+", " ");

		/* Split the contents of the extracted text with a space and put into array */
		String[] tag_data = textToExtract.split(" ");

		/* For each word in the array .. */
		for (String word : tag_data) {
			i++;
			/* If the queried word is found .. */
			if (word.toLowerCase().contains(WordCloudProcessor.wordcloud.word.toLowerCase())) {
				/* Add adjacent words with their adjacency frequency */
				scoreAdjacentWords(tag_data, word, i);
				/* Heuristic score for this URL should increase */
				tempHeuristic++;
			}
		}
		System.out.println(child + ": Heuristic Score => " + tempHeuristic);
		/* Map this URL and it's heuristic score */
		queue.offer(new UrlNode(child, tempHeuristic));
	}

	/*
	 * Very convuluted and overcomplicated abomination of a method, basically it
	 * gets the adjacent words to the query word instances within the text and
	 * shoves them into a map, mapping them with frequencies. It works I promise.
	 * Might come back and tidy it up at a later point
	 */
	public static void scoreAdjacentWords(String[] tag_data, String word, int i) {
		/* Words that come before and after the query word */
		String preceeding_word = tag_data[i - 1].toLowerCase();
		String proceeding_word = tag_data[i + 1].toLowerCase();

		/* Make sure to not count the plural of the query word */
		String ignorePlural = WordCloudProcessor.wordcloud.word.toLowerCase() + "s";

		/* As long as adjacent words aren't query words or plural of query .. */
		if (!preceeding_word.equals(ignorePlural) && !proceeding_word.equals(ignorePlural)
				&& !proceeding_word.equals(WordCloudProcessor.wordcloud.word.toLowerCase())
				&& !preceeding_word.equals(WordCloudProcessor.wordcloud.word.toLowerCase())) {
			/* Basically if it's already in map, increase freq, otherwise add it */
			if (WordCloudProcessor.associated_word_freq.containsKey(preceeding_word)) {
				WordCloudProcessor.associated_word_freq.put(preceeding_word,
						WordCloudProcessor.associated_word_freq.get(preceeding_word) + 1);
			} else if (WordCloudProcessor.associated_word_freq.containsKey(proceeding_word)) {
				WordCloudProcessor.associated_word_freq.put(proceeding_word,
						WordCloudProcessor.associated_word_freq.get(proceeding_word) + 1);
			} else {
				WordCloudProcessor.associated_word_freq.put(preceeding_word, 1);
				WordCloudProcessor.associated_word_freq.put(proceeding_word, 1);
			}
		}
	}
}
