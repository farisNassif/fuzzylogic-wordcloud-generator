package ancestor.attempt;

/* Simple node class that defines a URL object */
public final class UrlNode {
	private String url;
	private double score;
	private int depth;

	public UrlNode(String url, double d) {
		super();
		this.url = url;
		this.score = d;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public double getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
}
