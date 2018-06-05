package schedules;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import gui.SwingMain;
import s2pl.Phaser;
import serializers.conflict.CSR;
import serializers.finalState.FSR;
import serializers.view.VSR;
import utils.Decoder;
import utils.utils;

/**
 * 
 * @author Denis
 *
 */
public class Schedule {
	private LinkedList<Operation> operations;
	private HashSet<String> attributes = new HashSet<>();
	private HashMap<String, Transaction> transactions = new HashMap<>();
	private Schedule serialized;
	private LUSchedule luSchedule;

	public Schedule(LinkedList<Operation> operations) {
		this.operations = operations;

		for (Operation op : this.operations) {
			if (!transactions.containsKey(op.getTransaction())) {
				transactions.put(op.getTransaction(), new Transaction());
			}

			transactions.get(op.getTransaction()).addOperation(op);
			attributes.add(op.getAttribute());
		}
	}

	/**
	 * Builds every permutation of transactions of the current schedule.
	 * @return
	 */
	public HashSet<Schedule> createSerialSchedules() {
		HashSet<Schedule> returned = new HashSet<>();
		Object[] tKeys = transactions.keySet().toArray();
		HashSet<Object[]> tKeysPerms = utils.createPermutations(tKeys);

		for (Object[] tKeysPerm : tKeysPerms) {
			String serialSchedule = "";
			// Build a permutated serial schedule.
			for (Object tKey : tKeysPerm) {
				serialSchedule += transactions.get(tKey).stringify();
			}
			returned.add(new Schedule(Decoder.decode(serialSchedule)));
		}
		return returned;
	}

	// -----------------------S2PL------------------------

	public int isS2PL() {
		return s2pl.Phaser.fillLU(this);
	}

	// -----------------------FSR-------------------------

	public boolean isFSR() {
		return FSR.isFSR(this);
	}

	// -----------------------VSR-------------------------

	public boolean isVSR() {
		return VSR.isVSR(this);
	}

	// -----------------------CSR-------------------------

	public boolean isCSR(SwingMain returnGui) {
		return CSR.isCSR(this, returnGui);
	}

	// -----------------------Tests------------------------

	/**
	 * Returns the schedule written as it should be inserted into the decoder.
	 * @return
	 */
	public String stringify() {
		String returned = "";
		for (Operation op : operations) {
			if (op.getType().equals("r") || op.getType().equals("w"))
				returned += op.getType() + op.getTransaction() + "(" + op.getAttribute() + ")";
			else {
				returned += op.getType() + op.getTransaction();
			}
		}
		
		return returned;
	}

	// -----------------------Getters------------------------

	public LinkedList<Operation> getOperations() {
		return operations;
	}

	public HashMap<String, Transaction> getTransactions() {
		return transactions;
	}

	public Schedule getSerialized() {
		return serialized;
	}

	public void setSerialized(Schedule schedule) {
		serialized = schedule;
	}
	
	public HashSet<String> getAttributes(){
		return attributes;
	}
	
	public LUSchedule getLUSchedule(){
		return luSchedule;
	}
	
	public void setLUSchedule(LUSchedule schedule){
		luSchedule = schedule;
	}
}