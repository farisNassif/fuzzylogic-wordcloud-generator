package ie.gmit.sw.final_test;

public class FinalRunner {

	public static void main(String[] args) {
		/* Have params be input via tomcat in the future */
		Wordcloud wordcloud = new Wordcloud("gmit", 2, 20);

		/* Process the wordcloud */
		Thread thread = new Thread(new WordCloudProcessor(wordcloud));
		thread.start();
	}
}
