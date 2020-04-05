package ie.gmit.sw.final_test;

/* Generic word cloud object defining key values */
public class WordCloud {
	String word;
	int brachingFactor;
	int maxDepth;
	
	public WordCloud(String word, int brachingFactor, int maxDepth) {
		super();
		this.word = word;
		this.brachingFactor = brachingFactor;
		this.maxDepth = maxDepth;
	}
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int getBrachingFactor() {
		return brachingFactor;
	}
	public void setBrachingFactor(int brachingFactor) {
		this.brachingFactor = brachingFactor;
	}
	public int getMaxDepth() {
		return maxDepth;
	}
	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}
}
