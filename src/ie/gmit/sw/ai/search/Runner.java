package ie.gmit.sw.ai.search;

public class Runner {
	public static void main(String[] args) {
		Wordcloud wordcloud = new Wordcloud("software", 4, 3);
		WordcloudProcessor wordcloudProcessor = new WordcloudProcessor(wordcloud);

		/* Process the wordcloud */
		Thread thread = new Thread(new WordcloudProcessor(wordcloud));
		thread.start();
	}
}
