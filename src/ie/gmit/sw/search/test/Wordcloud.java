package ie.gmit.sw.search.test;

/* Generic word cloud object defining key values */
public class Wordcloud {
	String word;
	int brachingFactor;
	int maxDepth;
	
	public Wordcloud(String word, int brachingFactor, int maxDepth) {
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
