package ie.gmit.sw.ai.V3;

import org.jsoup.nodes.Document;

public final class DocumentNode {
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
