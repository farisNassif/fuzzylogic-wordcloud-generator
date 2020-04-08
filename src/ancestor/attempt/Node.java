package ancestor.attempt;

import java.util.*;

public class Node {
	private String url;
	private List<Node> children = new ArrayList<Node>();
	private boolean visited = false;

	public Node(String url) {
		this.url = url;
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

	public String getNodeName() {
		return url;
	}

	public void setNodeName(String nodeName) {
		this.url = nodeName;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public String toString() {
		return this.url;
	}
}