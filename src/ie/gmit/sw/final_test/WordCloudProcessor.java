package ie.gmit.sw.final_test;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/* Handles the internal processing of the wordcloud */
public class WordCloudProcessor implements Runnable {
	private Wordcloud wordcloud; /* Wordcloud object containing query word, branching factor and max depth */
	private static Set<String> closed_list = new ConcurrentSkipListSet<>();

	public WordCloudProcessor(Wordcloud wordcloud) {
		super();
		this.wordcloud = wordcloud;
	}

	@Override
	public void run() {
		System.out.println("Processing wordcloud ...");
		try {
			/* Start processing */
			InitializeSearch();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finished");
	}

	/* Kicks off the search */
	public void InitializeSearch() throws IOException {
		String initial_url = "https://duckduckgo.com/html/?q=";

		/* Kick off URL search and add initial URL to the closed list */
		Document doc = Jsoup.connect(initial_url + wordcloud.word).get();
		closed_list.add(initial_url);

		/* Get the resulting links of the initial URL and query text */
		Elements elements = doc.select("a");

		/* Generate child URL nodes based on initial query */
		GenerateChildNodes(elements);
	}

	/* Gen nodes based on initial search, discards pointless URLs / visited URLs */
	public void GenerateChildNodes(Elements elements) {
		/* Variable to just control branching factor */
		int birthControl = 1;
		for (Element e : elements) {
			/* Get absolute URL and clean any HTML syntax off it */
			String link = e.attr("href");

			/* Making sure to only check links that are worthy and not pointless */
			if (!closed_list.contains(link) && link.contains("https://") && birthControl <= wordcloud.brachingFactor) {
				/* Once this statement executes n times, stop */
				birthControl++;
				/* Was just visited, add to closed list to make sure don't visit it again */
				closed_list.add(link);
				System.out.println(link);
			}
		}
	}
}
