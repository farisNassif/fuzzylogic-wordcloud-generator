package temp;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Runner {
	public static void main(String[] args) {
		Document doc = null;
		try {
			doc = Jsoup.connect("https://duckduckgo.com/html/?q=software").userAgent(
					"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
					.referrer("http://www.google.com").execute().parse();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Elements children = doc.select("a");

		for (Element child : children) {
			String link = child.attr("href");
			System.out.println(link);
		}
	}
}
