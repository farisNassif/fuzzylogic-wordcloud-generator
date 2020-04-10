package ie.gmit.sw.ai.search;

/* Simple node class that defines a child object */
public final class Node implements Comparable<Node> {
	private String url;
	private double score;
	private int depth;

	public Node(String url, int depth) {
		super();
		this.url = url;
		this.depth = depth;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
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

	public void setScore(double score) {
		this.score = score;
	}

	@Override
	public int compareTo(Node node) {
		return 0;
	}

}
