package serializers.finalState;

import java.util.HashMap;
import java.util.HashSet;

import schedules.Schedule;
import serializers.finalState.HerbrandSemantics.HFunction;

public class FSR {

	/**
	 * Returns true if s is Final-State Serialisable
	 * @param s
	 * @return
	 */
	public static boolean isFSR(Schedule s) {
		HashMap<String, HerbrandSemantics.HFunction> herbrand = HerbrandSemantics.createHerbrandSemantics(s);
		HashSet<Schedule> serialSchedules = s.createSerialSchedules();
		
		for (Schedule ssched : serialSchedules) {
			HashMap<String, HerbrandSemantics.HFunction> serialHerbrand = HerbrandSemantics
					.createHerbrandSemantics(ssched);		
			// Stop when H(s) = H(serialS) and return true.
			// If never stopped, return false;
			int i = 0;
			for (String shkey : serialHerbrand.keySet()) {
				if (!HerbrandSemantics.compareHFunctions(herbrand.get(shkey), serialHerbrand.get(shkey))) {
					break;
				}

				if (++i == herbrand.size()) {
					s.setSerialized(ssched);
					return true;
				}
			}
		}

		return false;
	}

	public static void printHerbrand(Schedule s) {
		HashMap<String, HerbrandSemantics.HFunction> herbrand = HerbrandSemantics.createHerbrandSemantics(s);

		for (String hKeys : herbrand.keySet()) {
			System.out.println(HerbrandSemantics.stringify(herbrand.get(hKeys)));
		}
	}
}
