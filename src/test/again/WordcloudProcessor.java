package test.again;

import java.util.Map;

public class WordcloudProcessor {

	private Wordcloud wordcloud;
	private int search;

	public WordcloudProcessor(Wordcloud wordcloud, int search) {
		super();
		this.wordcloud = wordcloud;
		this.search = search;
	}

	public Map<String, Integer> process() {
		BestFirstSearch search = new BestFirstSearch(wordcloud);
		return search.ExecuteSearch();
	}

}
