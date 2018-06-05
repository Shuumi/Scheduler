package s2pl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import main.Scheduler;
import schedules.LUSchedule;
import schedules.Operation;
import schedules.Schedule;

public class Phaser {
	// Maps blocked attributes to the currently locking transaction
	private static HashMap<String, HashSet<String>> readlockedByTransaction;
	private static HashMap<String, String> writelockedByTransaction;
	// Maps transactions to all variables which were already locked by it (only
	// writelocks).
	private static HashMap<String, HashSet<String>> previouslyLocked;

	/**
	 * Saves the schedule s with lock and unlock operations into s.LUSchedule.
	 * In addition to that returns the informations about the phasing of the schedule s.
	 * @param s
	 * @return 0 if s follows s2pl, 1 if it only follows 2pl, 2 if it doesn't follow any of those.
	 */
	public static int fillLU(Schedule s) {
		int returned = 0;
		readlockedByTransaction = new HashMap<>();
		writelockedByTransaction = new HashMap<>();
		previouslyLocked = new HashMap<>();

		LinkedList<Operation> ops = s.getOperations();
		LinkedList<Operation> luops = (LinkedList<Operation>) s.getOperations().clone();

		int currInd = 0;
		for (Operation op : ops) {
			if (op.getAttribute() != null && !readlockedByTransaction.containsKey(op.getAttribute())) {
				readlockedByTransaction.put(op.getAttribute(), new HashSet<>());
			}
			if (!previouslyLocked.containsKey(op.getTransaction())) {
				previouslyLocked.put(op.getTransaction(), new HashSet<>());
			}

			// if reads: check whether the attribute is not already writelocked
			// by a transaction. If it is, then check
			// whether it's locked by the current transaction and if it isn't,
			// then unlock.
			// If not writelocked, then check whether it is readlocked by the
			// current transaction. If not, readlock it.
			if (op.getType().equals("r")) {
				if (writelockedByTransaction.keySet().contains(op.getAttribute())) {
					String lockingTransaction = writelockedByTransaction.get(op.getAttribute());
					if (!lockingTransaction.equals(op.getTransaction())) {
						luops.add(currInd++, new Operation("u", op.getAttribute(), lockingTransaction));
						readlockedByTransaction.get(op.getAttribute()).add(op.getTransaction());

						readlockedByTransaction.get(op.getAttribute()).remove(lockingTransaction);
						writelockedByTransaction.remove(op.getAttribute());
						previouslyLocked.get(lockingTransaction).add(op.getAttribute());

						luops.add(currInd++, new Operation("rl", op.getAttribute(), op.getTransaction()));
					}
				} else {
					if (readlockedByTransaction.get(op.getAttribute()).add(op.getTransaction()))
						luops.add(currInd++, new Operation("rl", op.getAttribute(), op.getTransaction()));
				}
			}

			// if writes: First check whether this transaction didn't unlock
			// yet. If it did unlock and writes again, then it can not be in
			// s2pl.
			// Next check whether current attribute is writelocked. If it is,
			// then check whether the locking transaction is the current one. If
			// it isn't then unlock from the locking transaction and lock to the
			// current one. Since it was writelocked, add the unlocked
			// transaction to the previouslyLocked.
			// If it is not writelocked, then remove every readlock that is made
			// upon the given attribute and is not made by the current
			// transaction. Lock it to the current transaction.
			// Lastly if the previouslyLocked contains the current transaction
			// and this transaction locks again, then this schedule does not
			// follow 2pl.
			if (op.getType().equals("w")) {
				boolean justLocked = false;

				if (returned == 0 && !previouslyLocked.get(op.getTransaction()).isEmpty()) {
					returned = 1;
				}

				if (writelockedByTransaction.keySet().contains(op.getAttribute())) {
					String lockingTransaction = writelockedByTransaction.get(op.getAttribute());
					if (!lockingTransaction.equals(op.getTransaction())) {
						luops.add(currInd++, new Operation("u", op.getAttribute(), lockingTransaction));
						readlockedByTransaction.get(op.getAttribute()).remove(lockingTransaction);
						readlockedByTransaction.get(op.getAttribute()).add(op.getTransaction());
						writelockedByTransaction.put(op.getAttribute(), op.getTransaction());

						previouslyLocked.get(lockingTransaction).add(op.getAttribute());
						luops.add(currInd++, new Operation("wl", op.getAttribute(), op.getTransaction()));
						justLocked = true;
					}
				} else {
					HashSet<String> toRemove = new HashSet<>();
					for (String lockingTransaction : readlockedByTransaction.get(op.getAttribute())) {
						if (!lockingTransaction.equals(op.getTransaction())) {
							luops.add(currInd++, new Operation("u", op.getAttribute(), lockingTransaction));
							toRemove.add(lockingTransaction);
						}
					}
					readlockedByTransaction.get(op.getAttribute()).removeAll(toRemove);
					readlockedByTransaction.get(op.getAttribute()).add(op.getTransaction());
					writelockedByTransaction.put(op.getAttribute(), op.getTransaction());
					luops.add(currInd++, new Operation("wl", op.getAttribute(), op.getTransaction()));
					justLocked = true;
				}
				if (previouslyLocked.keySet().contains(op.getTransaction())) {
					if (!previouslyLocked.get(op.getTransaction()).isEmpty() && justLocked) {
						returned = 2;
					}
				}
			}

			// on commit or abort clear all locks by this transaction
			if (op.getType().equals("c") || op.getType().equals("a")) {
				for (String attribute : readlockedByTransaction.keySet()) {
					readlockedByTransaction.get(attribute).remove(op.getTransaction());
				}
				HashSet<String> toRemove = new HashSet<>();
				for (String attribute : writelockedByTransaction.keySet()) {
					if (writelockedByTransaction.get(attribute).equals(op.getTransaction())) {
						toRemove.add(attribute);
					}
				}
				for(String attribute : toRemove){
					writelockedByTransaction.remove(attribute);
				}
			}
			currInd++;
		}
		s.setLUSchedule(new LUSchedule(luops));
		return returned;
	}
}