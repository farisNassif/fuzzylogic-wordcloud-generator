package legacy;

import ie.gmit.sw.ai.cloud.WordFrequency;

public class WordcloudProcessor {

	private Wordcloud wordcloud;
	private int searchType;

	public WordcloudProcessor(Wordcloud wordcloud, int searchType) {
		super();
		this.wordcloud = wordcloud;
		this.searchType = searchType;
	}

	public WordFrequency[] process() {
		Search search = null;
		switch (searchType) {
		case 1:
			search = new BestFirstSearch(wordcloud);
			break;
		case 2:
			// search = new BeamSearch(wordcloud);
			break;
		}
		return search.ExecuteSearch();
	}
}
