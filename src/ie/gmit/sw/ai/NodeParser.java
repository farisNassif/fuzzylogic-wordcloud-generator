package ie.gmit.sw.ai;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NodeParser {
	private final int MAX_VISITED = 100;
	private final int TITLE_WEIGHT = 20;
	private final int H1_WEIGHT = 15;
	private final int H2_WEIGHT = 11;
	private final int H3_WEIGHT = 8;
	private final int PARAGRAPH_WEIGHT = 6;

	private Map<String, Integer> map = new ConcurrentHashMap<>();
	
	private Set<String> closed_list = new ConcurrentSkipListSet<>();
	private Queue<DocumentNode> queue = new PriorityQueue<>(Comparator.comparing(DocumentNode::getScore));
	private String term;

	public static void main(String[] args) throws Throwable {
		new NodeParser("https://jsoup.org/cookbook/input/parse-document-from-string", "gmit");
	}

	public NodeParser(String url, String searchTerm) throws Throwable {
		this.term = searchTerm;
		Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
		int score = getHeuristicScore(doc);
		closed_list.add(url);
		queue.offer(new DocumentNode(doc, score));
		process();
	}

	private int getHeuristicScore(Document doc) {
		int score = 0;
		
		String title = doc.title();
		score += getFrequency(title);
		System.out.println(closed_list.size() + "-->" + title);
		
		Elements headings = doc.select("h1");
		for (Element heading : headings) {
			String h1 = heading.text();
			score += getFrequency(h1);
			System.out.println("\t" + h1);
		}
		
		// String body = doc.body().text();
		// score += getFrequency(body) + PARAGRAPH_WEIGHT;
		
		return 0;
	}
	
	private int getFrequency(String s) {
		// Check for term in s
		return 1;
	}
	
	private int getFuzzyHeuristic(int title, int headings, int body) {
		// http://prntscr.com/ra15m0
		return 1;
	}
	
	public void process() throws Throwable {
		while (!queue.isEmpty() && closed_list.size() <= MAX_VISITED) {
			DocumentNode node = queue.poll();
			Document doc = node.getDocument();
			
			Elements edges = doc.select("a[href]");
			
			for (Element e : edges) {
				String link = e.absUrl("href");
				
				if(link != null && !closed_list.contains(link) && closed_list.size() <= MAX_VISITED && link.contains("https") && !link.contains("linkedin")) {
					
					System.out.println(link);
					Document child_node = Jsoup.connect(link).get();
					
					int score = getHeuristicScore(child_node);
					queue.offer(new DocumentNode(child_node, score));
					
				}
			}
		}
	}

	private class DocumentNode {
		private Document document;
		private int score;

		public DocumentNode(Document document, int score) {
			super();
			this.document = document;
			this.score = score;
		}

		public Document getDocument() {
			return document;
		}

		public void setDocument(Document document) {
			this.document = document;
		}

		public int getScore() {
			return score;
		}

		public void setScore(int score) {
			this.score = score;
		}
	}
}
