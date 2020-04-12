package ie.gmit.sw.search.test;

public class Runner {

	public static void main(String[] args) {
		Wordcloud wordcloud = new Wordcloud("gmit", 4, 3);
		
		/* Process the wordcloud */
		Thread thread = new Thread(new BestFirstSearch(wordcloud));
		//Thread thread = new Thread(new BeamSearch(wordcloud));
		thread.start();
	}
}
