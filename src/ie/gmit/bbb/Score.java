package ie.gmit.bbb;

import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Queue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/* Initially had the contents of this class in WordCloudProcessor, stuck them in here to clean it up a bit */
public class Score {
	/*
	 * Very convuluted and overcomplicated abomination of a method, basically it
	 * gets the adjacent words to the query word instances within the text and
	 * shoves them into a map, mapping them with frequencies. It works I promise.
	 * Might come back and tidy it up at a later point.
	 * 
	 * OUT OF ACTION FOR NOW
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
