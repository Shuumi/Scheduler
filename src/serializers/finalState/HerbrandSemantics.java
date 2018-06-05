package serializers.finalState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import schedules.Operation;
import schedules.Schedule;

public class HerbrandSemantics {

	/**
	 * Returns a HashMap mapping each attribute to the appropriate for s
	 * HFunction. Use stringify to make it readable.
	 * @param s
	 * @return
	 */
	public static HashMap<String, HFunction> createHerbrandSemantics(Schedule s) {
		// Key is the attribute name and value is the current value
		HashMap<String, HFunction> attributeValues = new HashMap<>();
		// Key is the transaction name and value are values read
		HashMap<String, LinkedList<HFunction>> readByTransaction = new HashMap<>();

		HashSet<String> abortedTransactions = new HashSet<>();

		// We can ignore aborted transactions
		for (String tKey : s.getTransactions().keySet()) {
			if (s.getTransactions().get(tKey).isAborted()) {
				abortedTransactions.add(tKey);
			}
		}

		// setup: starting attribute values are f0,_()
		// setup: starting readByTransaction is nothing
		for (Operation op : s.getOperations()) {
			if (!abortedTransactions.contains(op.getTransaction()) && op.getAttribute() != null) {
				attributeValues.putIfAbsent(op.getAttribute(), new HFunction(op.getAttribute(), "0"));
				readByTransaction.put(op.getTransaction(), new LinkedList<>());
			}
		}

		for (Operation op : s.getOperations()) {
			if (!abortedTransactions.contains(op.getTransaction())) {
				// if reads and did not read the current value before, add in
				// readByTransaction.
				if (op.getType().equals("r")) {
					if (!alreadyRead(readByTransaction.get(op.getTransaction()),
							attributeValues.get(op.getAttribute())))
						readByTransaction.get(op.getTransaction()).add(attributeValues.get(op.getAttribute()));
				}

				// if writes, update attributeValues.
				// Assign the attribute values to a new HerbrandFunction which
				// depends on all readByTransactions of this transaction.
				if (op.getType().equals("w")) {
					HFunction toAssign = new HFunction(op.getAttribute(), op.getTransaction());
					for (HFunction hf : readByTransaction.get(op.getTransaction())) {
						toAssign.addDependency(hf);
					}
					attributeValues.replace(op.getAttribute(), toAssign);
				}
			}
		}

		return attributeValues;
	}

	/**
	 * Returns true if HerbrandList contains the HElement.
	 * 
	 * @param HerbandList
	 *            A linked list of HFunctions
	 * @param HElement
	 *            An HFunction which should be checked whether it is contained
	 *            withing HerbrandList.
	 * @return
	 */
	public static boolean alreadyRead(LinkedList<HFunction> HerbandList, HFunction HElement) {
		for (HFunction herb : HerbandList) {
			if (compareHFunctions(herb, HElement)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns true if every aspect of the two given HFunctions is the same.
	 * 
	 * @param first
	 * @param second
	 * @return
	 */
	public static boolean compareHFunctions(HFunction first, HFunction second) {
		if (!first.attribute.equals(second.attribute) || !first.transaction.equals(second.transaction)) {
			return false;
		}

		if (first.deps.isEmpty() && second.deps.isEmpty()) {
			return true;
		}

		int i = 0;
		for (HFunction fDeps : first.deps) {
			for (HFunction sDeps : second.deps) {
				if (compareHFunctions(fDeps, sDeps)) {
					i++;
					break;
				}
			}
		}

		if (i == first.deps.size() && i == second.deps.size())
			return true;
		else
			return false;
	}

	/**
	 * Returns the HFunction written as in the lecture.
	 * 
	 * @param herbrand
	 * @return
	 */
	public static String stringify(HFunction herbrand) {
		boolean noDeps = true;
		String returned = "f" + herbrand.transaction + "," + herbrand.attribute + "(";
		for (HFunction deps : herbrand.deps) {
			noDeps = false;
			returned += stringify(deps) + ", ";
		}
		if (!noDeps)
			returned = returned.substring(0, returned.length() - 2);
		returned += ")";
		return returned;
	}

	public static class HFunction {
		private String attribute, transaction;
		private HashSet<HFunction> deps;

		private HFunction(String attribute, String transaction) {
			this.attribute = attribute;
			this.transaction = transaction;
			deps = new HashSet<>();
		}

		private void addDependency(HFunction dep) {
			deps.add(dep);
		}
	}
}
