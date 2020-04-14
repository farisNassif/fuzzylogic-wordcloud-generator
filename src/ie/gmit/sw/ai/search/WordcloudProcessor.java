package ie.gmit.sw.ai.search;

import ie.gmit.sw.ai.cloud.WordFrequency;

public class WordcloudProcessor {
	private Wordcloud wordcloud;
	private int searchType;
	private int branchingFactor;
	private int maxDepth;

	public WordcloudProcessor(Wordcloud wordcloud, int searchType, int branchingFactor, int maxDepth) {
		super();
		this.wordcloud = wordcloud;
		this.searchType = searchType;
		this.branchingFactor = branchingFactor;
		this.maxDepth = maxDepth;
	}

	public WordFrequency[] process() {
		Search search = null;
		switch (searchType) {
		case 1:
			search = new BestFirstSearch(wordcloud, branchingFactor, maxDepth);
			break;
		case 2:
			search = new BeamSearch(wordcloud, branchingFactor, maxDepth);
			break;
		}
		return search.ExecuteSearch();
	}

	public int getBranchingFactor() {
		return branchingFactor;
	}

	public void setBranchingFactor(int branchingFactor) {
		this.branchingFactor = branchingFactor;
	}

	public int getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}
}
