
import java.util.*;

public class BeamSearch {
	// LinkedList<Node> queue = new LinkedList<Node>();
	private Deque<Node> queue = new LinkedList<>();
	HeuristicNodeComparator sorter = new HeuristicNodeComparator();
	int beamWidth = 3;
	int totalDistnace = 0;

	public void search(Node node) {
		node = queue.poll();
		node.setVisited(true);
		totalDistnace = totalDistnace + node.getDistance(queue.getFirst());
		queue.addFirst(node);
		int totalDistance = 0;
		while (!queue.isEmpty()) {
			queue.removeFirst();
			System.out.print("Visiting " + node.getNodeName() + "\t");
			if (node.isGoalNode()) {
				System.out.println("Reached goal node " + node.getNodeName() + " after " + totalDistance + " miles.");
				System.exit(0);
			} else {
				Node[] children = node.children();
				Arrays.sort(children, Comparator.comparing(Node::getApproximateDistanceFromGoal));

				int bound = (children.length < beamWidth) ? children.length : beamWidth;

				for (int i = 0; i < bound; i++) {
					if (!children[i].isVisited()) {
						queue.offer(children[i]);
					}
				}
				System.out.println(queue);

			}
		}
	}

	public static void main(String[] args) {
		IrelandMap ire = new IrelandMap();
		Node start = ire.getStartNode();
		start.setVisited(true);
		BeamSearch bf = new BeamSearch();
		bf.search(start);
	}
}
