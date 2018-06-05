package serializers.view;

import java.util.HashMap;
import java.util.HashSet;

import schedules.Operation;
import schedules.Schedule;
import schedules.Transaction;

public class ReadsFrom {
	private String from, to;
	private String attribute;

	public ReadsFrom(String from, String to, String attribute) {
		this.from = from;
		this.to = to;
		this.attribute = attribute;
	}

	/**
	 * Returns true if both ReadsFroms are same in every aspect.
	 * 
	 * @param rf1
	 * @param rf2
	 * @return
	 */
	public static boolean compareRF(ReadsFrom rf1, ReadsFrom rf2) {
		return rf1.attribute.equals(rf2.attribute) && rf1.from.equals(rf2.from) && rf1.to.equals(rf2.to);
	}

	/**
	 * Returns true if the container contains the element as defined by
	 * compareRF.
	 * 
	 * @param container
	 * @param element
	 * @return
	 */
	public static ReadsFrom containsRF(HashSet<ReadsFrom> container, ReadsFrom element) {
		for (ReadsFrom conele : container) {
			if (compareRF(conele, element)) {
				return conele;
			}
		}
		return null;
	}

	/**
	 * Returns true if both HashSets contain the same ReadsFrom as defined by
	 * compareRF.
	 * 
	 * @param rf1
	 * @param rf2
	 * @return
	 */
	public static boolean areSame(HashSet<ReadsFrom> rf1, HashSet<ReadsFrom> rf2) {
		HashSet<ReadsFrom> rf2copy = (HashSet<ReadsFrom>) rf2.clone();

		for (ReadsFrom rf : rf1) {
			ReadsFrom todel;
			if ((todel = containsRF(rf2copy, rf)) != null) {
				rf2copy.remove(todel);
			} else {
				return false;
			}
		}

		return rf2copy.isEmpty();
	}

	/**
	 * Creates ReadsFrom groups. Use stringify to make it readable as in the
	 * lecture.
	 * 
	 * @param s
	 * @return
	 */
	public static HashSet<ReadsFrom> createRF(Schedule s) {
		HashSet<ReadsFrom> returned = new HashSet<>();
		// Saves which transaction wrote last on each attribute
		// Key is the attribute and the value is the transaction
		HashMap<String, String> currentlyWritten = new HashMap<>();

		HashSet<String> abortedTransactions = new HashSet<>();

		// We can ignore aborted transactions
		for (String tKey : s.getTransactions().keySet()) {
			if (s.getTransactions().get(tKey).isAborted()) {
				abortedTransactions.add(tKey);
			}
		}

		// Setup: each attribute was last assigned by transaction "^"
		for (Operation op : s.getOperations()) {
			if (!abortedTransactions.contains(op.getTransaction())
					&& (op.getType().equals("r") || op.getType().equals("w")))
				currentlyWritten.putIfAbsent(op.getAttribute(), "^");
		}

		for (Operation op : s.getOperations()) {
			if (!abortedTransactions.contains(op.getTransaction())) {
				// if reads and the same ReadsFrom does not yet exist in
				// returned, add the proper ReadsFrom to returned
				if (op.getType().equals("r")) {
					ReadsFrom currRF = new ReadsFrom(currentlyWritten.get(op.getAttribute()), op.getTransaction(),
							op.getAttribute());
					if (containsRF(returned, currRF) == null) {
						returned.add(currRF);
					}
				}

				// if writes, replace the written attribute in currentlyWritten
				// to the current transactions' name
				if (op.getType().equals("w")) {
					currentlyWritten.replace(op.getAttribute(), op.getTransaction());
				}
			}
		}

		// add the final ReadsFroms. The last transactions' name is "$"
		for (String attrib : currentlyWritten.keySet()) {
			String tran = currentlyWritten.get(attrib);
			returned.add(new ReadsFrom(tran, "$", attrib));
		}

		return returned;
	}

	/**
	 * Returns the HashSet of readsfroms in a string which looks like alike to
	 * those shown in the lecture.
	 * @param rfs
	 * @return
	 */
	public static String stringify(HashSet<ReadsFrom> rfs) {
		String returned = "{";
		boolean isEmpty = true;

		for (ReadsFrom rf : rfs) {
			isEmpty = false;
			returned += "(T-" + rf.from + ", " + rf.attribute + ", T-" + rf.to + "), ";
		}

		if (!isEmpty) {
			returned = returned.substring(0, returned.length() - 2);
		}

		return returned + "}";
	}

	// -----------------------Getters------------------------

	public String getFromT() {
		return from;
	}

	public String getToT() {
		return to;
	}

	public String getAttribute() {
		return attribute;
	}
}
