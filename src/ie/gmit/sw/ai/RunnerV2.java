package ie.gmit.sw.ai;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RunnerV2 {

	public static void main(String[] args) throws Throwable {
		List<String> urls = new ArrayList<String>();
		
		String query = "GMIT Software";
		Document doc = Jsoup.connect("https://duckduckgo.com/html/?q=" + query).get();
		Elements res = doc.getElementById("links").getElementsByClass("results_links");

		for (Element r : res) {
			Element title = r.getElementsByClass("links_main").first().getElementsByTag("a").first();
			urls.add(title.attr("href"));
		}
		
		for (String s : urls) {
			System.out.println(s);
		}
	}
}
