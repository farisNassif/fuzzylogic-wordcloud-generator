package ie.gmit.sw.ai.search;

/* Simple node class that defines a child object */
public final class Node implements Comparable<Node> {
	public boolean isTraversed = false; // For beam
	private String url;
	private Node parent;
	private double score;
	private int depth;

	/* For initial node */
	public Node(String url, int depth) {
		super();
		this.url = url;
		this.depth = depth;
	}

	/* For every other child */
	public Node(String url, Node parent, int depth) {
		super();
		this.url = url;
		this.depth = depth;
		this.parent = parent;
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

	public boolean isTraversed() {
		return isTraversed;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	@Override
	public int compareTo(Node node) {
		return 0;
	}

}
