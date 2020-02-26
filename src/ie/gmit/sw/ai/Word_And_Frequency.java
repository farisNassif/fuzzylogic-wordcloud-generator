package ie.gmit.sw.ai;

public class Word_And_Frequency implements Comparable<Word_And_Frequency> {
	private String word;
	private int frequency;
	private int fontSize;
	
	public Word_And_Frequency(String word, int frequency) {
		super();
		this.word = word;
		this.frequency = frequency;
	}

	public String getWord() {
		return word;
	}

	public void setKmer(String word) {
		this.word = word;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	public int getFontSize() {
		return this.fontSize;
	}

	public void setFontSize(int size) {
		this.fontSize = size;
	}
	
	@Override
	public int compareTo(Word_And_Frequency next) {
		return -Integer.compare(frequency, next.getFrequency());
	}
}
