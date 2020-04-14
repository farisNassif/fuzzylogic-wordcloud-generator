package ie.gmit.sw.ai.search;

/* Generic word cloud object defining key values */
public class Wordcloud {
	private String word;
	private int maxWords;

	public Wordcloud(String word, int maxWords) {
		super();
		this.word = word;
		this.maxWords = maxWords;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getMaxWords() {
		return maxWords;
	}

	public void setMaxWords(int maxWords) {
		this.maxWords = maxWords;
	}

}
