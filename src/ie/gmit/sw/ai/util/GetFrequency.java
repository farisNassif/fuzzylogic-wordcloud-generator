package ie.gmit.sw.ai.util;

import java.util.Map;
import java.util.Map.Entry;

import ie.gmit.sw.ai.cloud.WordFrequency;
import ie.gmit.sw.ai.search.Wordcloud;

/* Generate and return frequency table */
public class GetFrequency {
	public static WordFrequency[] GenerateFrequency(Map<String, Integer> sortedFrequencyMap, Wordcloud wordcloud) {
		int count = 0;
		WordFrequency[] wf = new WordFrequency[wordcloud.getMaxWords()];

		for (Entry<String, Integer> word : sortedFrequencyMap.entrySet()) {
			if (count >= wordcloud.getMaxWords()) {
				break;
			} else {
				wf[count] = new WordFrequency(word.getKey(), word.getValue());
				count++;
			}
		}
		return wf;
	}
}
