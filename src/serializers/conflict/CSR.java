package serializers.conflict;

import java.util.HashSet;

import gui.DrawGraph;
import gui.SwingMain;
import schedules.Schedule;

public class CSR {

	/**
	 * returns true if s is Conflict Serialisable. Also passes informations
	 * needed for the drawing of the conflict graph.
	 * 
	 * @param s
	 * @param returnGUI
	 * @return
	 */
	public static boolean isCSR(Schedule s, SwingMain returnGUI) {
		HashSet<ConflictGraph.Node> sCG = ConflictGraph.createConflictGraph(s);
		if (returnGUI != null)
			returnGUI.setGraph(new DrawGraph(sCG));

		for (Schedule serS : s.createSerialSchedules()) {
			if (ConflictGraph.areEqual(sCG, ConflictGraph.createConflictGraph(serS))) {
				s.setSerialized(serS);
				return true;
			}
		}

		return false;
	}
}
