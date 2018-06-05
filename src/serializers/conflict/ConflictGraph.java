package serializers.conflict;

import java.util.HashSet;

import schedules.Operation;
import schedules.Schedule;

public class ConflictGraph {

	/**
	 * Returns a hash set of nodes which build the conflict graph of the given schedule s.
	 * @param s
	 * @return
	 */
	public static HashSet<Node> createConflictGraph(Schedule s) {
		HashSet<Node> returned = new HashSet<>();

		HashSet<String> abortedTransactions = new HashSet<>();

		// since only reads do not conflict with each other and most schedules
		// start with them, we will not be checking until a write happens
		boolean wrote = false;

		// We can ignore aborted transactions
		for (String tKey : s.getTransactions().keySet()) {
			if (s.getTransactions().get(tKey).isAborted()) {
				abortedTransactions.add(tKey);
			}
		}
		for (Operation op : s.getOperations()) {
			if (!abortedTransactions.contains(op.getTransaction()) &&(wrote || op.getType().equals("w"))) {
				wrote = true;

				for (Operation op2 : s.getOperations()) {
					// Conflicts happen only if both operations are not reads,
					// transactions of those operations differ and attributes
					// are equal
					if (!(op.getType().equals("r") && op2.getType().equals("r"))
							&& op.getAttribute() != null && op2.getAttribute() != null
							&& !op.getTransaction().equals(op2.getTransaction())
							&& op.getAttribute().equals(op2.getAttribute())
							&& !abortedTransactions.contains(op2.getTransaction())) {
						Node toAdd = new Node(op2.getTransaction(), op.getTransaction());
						if (containsNode(returned, toAdd) == null) {
							returned.add(toAdd);
						}
					}
					if (op2 == op) {
						break;
					}
				}
			}
		}

		return returned;
	}

	private static boolean areEqual(Node n1, Node n2) {
		return n1.start.equals(n2.start) && n1.end.equals(n2.end);
	}

	public static boolean areEqual(HashSet<Node> n1, HashSet<Node> n2) {
		HashSet<Node> n2copy = (HashSet<Node>) n2.clone();
		for (Node n : n1) {
			Node toDel;
			if ((toDel = containsNode(n2copy, n)) != null) {
				n2copy.remove(toDel);
			} else {
				return false;
			}
		}

		return n2copy.isEmpty();
	}

	private static Node containsNode(HashSet<Node> set, Node node) {
		for (Node n : set) {
			if (areEqual(node, n)) {
				return n;
			}
		}
		return null;
	}
	
	// -----------------------Cycles------------------------
	/**
	 * Returns true if there exist a cycle within the given graph. (Not used in the actual implementation)
	 * @param graph
	 * @return
	 */
	public static boolean hasCycles(HashSet<Node> graph) {
		HashSet<String> startedWith = new HashSet<>();
		
		// if it is possible to go from starting pos back to starting pos, there
		// exists a cycle.
		// otherwise change the starting pos and repeat.
		// if no other untouched starting pos exists and cycle wasn't found, there
		// exist no cycles.
		for (Node node : graph) {
			if (!startedWith.contains(node.start)) {
				if (hasCyclesWithStartPoint(node, graph)) {
					return true;
				}
				startedWith.add(node.start);
			}
		}

		return false;
	}

	/**
	 * Returns true if there exist a directed path in graph which starts and ends at startingNode.
	 * @param startingNode
	 * @param graph
	 * @return
	 */
	private static boolean hasCyclesWithStartPoint(Node startingNode, HashSet<Node> graph) {
		HashSet<String> reachedNodes = new HashSet<>();
		reachedNodes.add(startingNode.start);
		reachedNodes.add(startingNode.end);

		String startingString = startingNode.start;

		boolean repeat = true;
		while (repeat) {
			repeat = false;
			for (Node node : graph) {
				// if we have started the search (if we have found a valid
				// starting point) and if the node's starting point is contained
				// within reachedNodes, add nodes' destination into reachedNodes
				// If nodes' end equals the start, then a cycle exists
				if (reachedNodes.contains(node.start) && node.end.equals(startingString)) {
					return true;
				}
				if (reachedNodes.contains(node.start) && !reachedNodes.contains(node.end)) {
					reachedNodes.add(node.end);
					repeat = true;
				}
			}
		}
		return false;
	}

	public static void testCycle() {
		String a = "a", b = "b", c = "c", d = "d", e = "e", f = "f";
		Node ab = new Node(a, b);
		Node ac = new Node(a, c);
		Node ba = new Node(b, a);
		Node bd = new Node(b, d);
		Node ce = new Node(c, e);
		Node af = new Node(a, f);
		Node fb = new Node(f, b);
		Node bc = new Node(b, c);
		Node cf = new Node(c, f);

		HashSet<Node> g1 = new HashSet<>();
		g1.add(ab);
		g1.add(ba);

		HashSet<Node> g2 = new HashSet<>();
		g2.add(ab);
		g2.add(ac);
		g2.add(bd);
		g2.add(ce);
		g2.add(af);

		HashSet<Node> g3 = new HashSet<>();
		g3.add(ab);
		g3.add(af);
		g3.add(fb);

		HashSet<Node> g4 = new HashSet<>();
		g4.add(ba);
		g4.add(af);
		g4.add(fb);

		HashSet<Node> g5 = new HashSet<>();
		g5.add(ab);
		g5.add(ac);
		g5.add(af);
		g5.add(bd);
		g5.add(fb);

		HashSet<Node> g6 = new HashSet<>();
		g6.add(bc);
		g6.add(cf);
		g6.add(fb);
		
		HashSet<Node> g7 = new HashSet<>();
		g7.add(fb);
		g7.add(ab);
		g7.add(cf);
		g7.add(ac);
		g7.add(bd);
		g7.add(ce);
		g7.add(af);
		g7.add(bc);
		

		System.out.println(hasCycles(g1) + " true");
		System.out.println(hasCycles(g2) + " false");
		System.out.println(hasCycles(g3) + " false");
		System.out.println(hasCycles(g4) + " true");
		System.out.println(hasCycles(g5) + " false");
		System.out.println(hasCycles(g6) + " true");
		System.out.println(hasCycles(g7) + " true");
	}

	public static class Node {
		private String start, end;

		private Node(String start, String end) {
			this.start = start;
			this.end = end;
		}

		public String getStart() {
			return start;
		}

		public String getEnd() {
			return end;
		}
	}
}
