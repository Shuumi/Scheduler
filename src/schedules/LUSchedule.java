package schedules;

import java.util.LinkedList;

/**
 * Simplified schedule class. It can only stringify, since it is filled with
 * operations which are not accepted into further algorithms.
 * 
 * @author Denis
 *
 */
public class LUSchedule {
	private LinkedList<Operation> operations;

	public LUSchedule(LinkedList<Operation> operations) {
		this.operations = operations;
	}

	/**
	 * 
	 * @return
	 */
	public String stringify() {
		String returned = "";
		int i = 0;
		for (Operation op : operations) {
			i++;
			if (op.getType().equals("r") || op.getType().equals("w") || op.getType().equals("rl")
					|| op.getType().equals("wl") || op.getType().equals("u"))
				returned += op.getType() + op.getTransaction() + "(" + op.getAttribute() + ")";
			else {
				returned += op.getType() + op.getTransaction();
			}
			if (i % 25 == 0) {
				returned += "\n";
			}
		}

		return returned;
	}
}
