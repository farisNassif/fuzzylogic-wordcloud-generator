package ie.gmit.sw.ai.search;

import org.jsoup.select.Elements;

import ie.gmit.sw.ai.cloud.WordFrequency;
import ie.gmit.sw.ai.util.IgnoreWords;
import ie.gmit.sw.ai.util.Stopwatch;

/* Handles the internal processing of the wordcloud */
public class HillClimbingSearch extends Search {
	private Wordcloud wordcloud;
	private int branchingFactor;
	private int maxDepth;

	public HillClimbingSearch(Wordcloud wordcloud, int branchingFactor, int maxDepth) {
		this.wordcloud = wordcloud;
		this.branchingFactor = branchingFactor;
		this.maxDepth = maxDepth;
	}

	@Override
	WordFrequency[] ExecuteSearch() {
		Stopwatch stopwatch = new Stopwatch();

		stopwatch.start();

		try {
			InitializeSearch();
		} catch (Throwable e) {

			e.printStackTrace();
		}

		stopwatch.stop();
		System.out.println(stopwatch.toString());
		System.out.println("fin");
		return null;
	}

	/* Kicks off the search */
	private void InitializeSearch() throws Throwable {
		IgnoreWords.ignoreQuery(wordcloud.getWord());

		/* Construct the initial node */
		String queryUrl = "https://duckduckgo.com/html/?q=" + wordcloud.getWord();

		Node initial = new Node(queryUrl, 0);

		try {
			GenerateChildNodes(initial);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/* Generate children from a parent node */
	private void GenerateChildNodes(Node parent) throws Throwable {
		int count = 0;
		Elements children = null;

	}

}
