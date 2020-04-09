package ie.gmit.sw;

public class Runner {
	public static void main(String[] args) {
		Wordcloud wordcloud = new Wordcloud("ireland", 4, 3);
		
		/* Process the wordcloud */
		Thread thread = new Thread(new WordcloudProcessor(wordcloud));
		thread.start();
	}
}
