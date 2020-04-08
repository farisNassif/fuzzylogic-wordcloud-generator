package complete.deploy;

import java.util.ArrayList;
import java.util.List;

/* Simple node class that defines a child object */
public final class Node implements Comparable<Node> {
	private String url;
	private double score;
	private int depth;
	private List<Node> children = new ArrayList<Node>();

	public Node(String url, int depth) {
		super();
		this.url = url;
		this.depth = depth;
	}

	public Node[] children() {
		return (Node[]) children.toArray(new Node[children.size()]);
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

	public int getChildNodeCount() {
		return children.size();
	}

	public void addChildNode(Node child) {
		children.add(child);
	}

	public void removeChild(Node child) {
		children.remove(child);
	}

	@Override
	public int compareTo(Node arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
