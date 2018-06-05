package main;

import java.util.Arrays;
import java.util.LinkedList;

import gui.SwingMain;
import s2pl.Phaser;
import schedules.LUSchedule;
import schedules.Operation;
import schedules.Schedule;
import utils.Constants;
import utils.Decoder;
import utils.utils;

public class Scheduler {

	// public static void main(String[] args) {
	// SwingMain sm = new SwingMain();
	// }

	public static void beginScheduling(String schedule, SwingMain returnGUI) {
		LinkedList<Operation> ops = Decoder.decode(schedule);
		Schedule s = new Schedule(ops);

		if (s.isFSR()) {
			returnGUI.lblFsr.setBackground(returnGUI.green);
		} else {
			returnGUI.lblFsr.setBackground(returnGUI.red);
		}

		if (s.isVSR()) {
			returnGUI.lblVsr.setBackground(returnGUI.green);
		} else {
			returnGUI.lblVsr.setBackground(returnGUI.red);
		}

		if (s.isCSR(returnGUI)) {
			returnGUI.lblCsr.setBackground(returnGUI.green);
		} else {
			returnGUI.lblCsr.setBackground(returnGUI.red);
		}

		if (s.getSerialized() != null) {
			returnGUI.lblSerialize.setText(s.getSerialized().stringify());
		} else {
			returnGUI.lblSerialize.setText("");
		}

		int phase = s.isS2PL();
		if (phase == 2) {
			returnGUI.lblLU.setBackground(returnGUI.red);
		} else if (phase == 1) {
			returnGUI.lblLU.setBackground(returnGUI.yellow);
		} else {
			returnGUI.lblLU.setBackground(returnGUI.green);
		}
		returnGUI.lblLU.setText("<HTML>" + s.getLUSchedule().stringify() + "</HTML>");
	}

	public static void testPermutations(Object[] arr) {
		for (Object[] perm : utils.createPermutations((Object[]) arr)) {
			for (Object a : perm) {
				System.out.print(a + " ");
			}
			System.out.println();
		}
	}

	public static void testPermutations() {
		Object[] arr = { 1, 2, 3, 4, 5, 6 };
		int corrPermCount = 0;
		new Constants();
		while (!Constants.permutationsOfSix.isEmpty()) {
			Object[] corrPerm = Constants.permutationsOfSix.poll();
			for (Object[] perm : utils.createPermutations((Object[]) arr)) {
				if (Arrays.equals(perm, corrPerm)) {
					corrPermCount++;
				}
			}
		}
		if (corrPermCount == 720) {
			System.out.println("YUP!");
		} else {
			System.err.println("NO!");
		}
	}

	public static void testSerializers() {
		String[] schedules = { "r2(z)r1(x)w2(x)r4(x)r1(y)r4(y)w3(y)r4(z)w4(y)c1c2c3c4",
				"r1(x)r2(z)w3(y)r1(y)r2(x)r3(y)w1(x)w2(z)r3(z)w1(z)w3(x)c1c2c3",
				"r2(y)w2(y)r2(x)r1(y)r1(x)w1(x)w1(z)r3(z)r3(x)w3(z)c1c2c3",
				"r2(x)r1(y)r2(z)w1(y)w2(z)r1(z)w1(z)w2(y)w1(x)c1c2", "r1(a)r2(c)w2(a)w1(b)r2(b)w2(c)c2r1(c)w1(a)c1",
				"r2(b)r1(b)r2(a)r2(c)w1(c)w2(c)w2(a)c2w1(a)w1(b)c1w3(c)c3",
				"r1(a)r2(a)r1(c)r2(b)w1(d)w1(c)w1(a)a1r2(d)w2(d)c2",
				"r5(x)r2(z)r1(x)w2(x)r4(x)r1(y)r4(y)w3(y)r4(z)w4(y)c1c2c3c4w5(x)w5(y)w5(z)c5",
				"w1(x)r2(x)w2(x)r1(x)w1(x)c1c2w3(x)c3" };
		boolean[] results = { false, false, false, false, false, false, true, true, true, false, false, false, false,
				false, false, true, true, false, true, true, true, false, false, false, true, false, false };

		int i = 0;
		for (String schedule : schedules) {
			LinkedList<Operation> operations = Decoder.decode(schedule);
			Schedule s = new Schedule(operations);
			System.out.println(s.isFSR() + " " + results[i++]);
			System.out.println(s.isVSR() + " " + results[i++]);
			System.out.println(s.isCSR(null) + " " + results[i++]);
		}
	}
}
