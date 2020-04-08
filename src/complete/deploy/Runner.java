package complete.deploy;

public class Runner {
	public static void main(String[] args) {
		Wordcloud wordcloud = new Wordcloud("microsoft", 2, 3);

		/* Process the wordcloud */
		Thread thread = new Thread(new WordCloudProcessor(wordcloud));
		thread.start();
	}
}
