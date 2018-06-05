package utils;

import java.util.InputMismatchException;
import java.util.LinkedList;

import schedules.Operation;

/**
 * This class is used to decode a string containing a schedule. Only basic
 * operations (read[r], write[w], commit[c] and abort[a]) are allowed.
 * Transactions may be named in any way as long as they do not contain any
 * letter describing a transaction. After a transaction terminates (with "c" or
 * "a") no further operations can be added upon this transaction's name. Each
 * transaction must terminate. Each operation must contain an operation type
 * ({r,w,c,a}) and a transaction name. Additionally read and write transactions
 * must have written the attribute name inside of round parenthesis. No
 * additional information are allowed. Whitespace characters, as long as they
 * are not a part of any name, are ignored.
 * 
 * @author Denis
 *
 */
public class Decoder {
	private static boolean last = false;
	private static String currentlyDecoded;

	/**
	 * Returns a linked list of operations in order of happening in the schedule
	 * string.
	 * 
	 * @param schedule
	 * @return
	 */
	public static LinkedList<Operation> decode(String schedule) {
		last = false;
		currentlyDecoded = schedule;

		LinkedList<Operation> returned = new LinkedList<>();

		while (!last) {
			Operation op = getNextOp();
			if (op == null) {
				throw new InputMismatchException("wrong operation types. Accepted are r, w, a, c");
			}
			returned.add(op);
		}
		return returned;
	}

	/**
	 * Returns the first operation from the currentlyDecoded string. It also
	 * removes the currently decoded operation from currentlyDecoded.
	 * 
	 * @return
	 */
	private static Operation getNextOp() {
		Operation returned = null;
		currentlyDecoded = currentlyDecoded.trim();

		if (currentlyDecoded.startsWith("a") || currentlyDecoded.startsWith("c")) {
			String optype = "" + currentlyDecoded.charAt(0);
			currentlyDecoded = currentlyDecoded.substring(1);
			int nextOpPos = 0;
			for (int i = 0; i < currentlyDecoded.length(); i++) {
				if (currentlyDecoded.charAt(i) == 'r' || currentlyDecoded.charAt(i) == 'w'
						|| currentlyDecoded.charAt(i) == 'a' || currentlyDecoded.charAt(i) == 'c') {
					nextOpPos = i;
					break;
				}
			}
			if (nextOpPos == 0) {
				nextOpPos = currentlyDecoded.length();
				last = true;
			}

			String opattr = null;
			String optrans = currentlyDecoded.substring(0, nextOpPos).toLowerCase();

			currentlyDecoded = currentlyDecoded.substring(nextOpPos);

			returned = new Operation(optype, opattr, optrans);
		} else if (currentlyDecoded.startsWith("r") || currentlyDecoded.startsWith("w")) {
			int opBracketPos = currentlyDecoded.indexOf('(');
			int clBracketPos = currentlyDecoded.indexOf(')');

			String optype = currentlyDecoded.substring(0, 1).toLowerCase();
			String opattr = currentlyDecoded.substring(opBracketPos + 1, clBracketPos).toLowerCase();
			String optrans = currentlyDecoded.substring(1, opBracketPos).toLowerCase();

			currentlyDecoded = currentlyDecoded.substring(clBracketPos + 1);

			returned = new Operation(optype, opattr, optrans);
		}
		return returned;
	}
}
