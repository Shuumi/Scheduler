package serializers.view;

import java.util.HashMap;
import java.util.HashSet;

import schedules.Operation;
import schedules.Schedule;

public class VSR {
	
	/**
	 * Returns true if the transaction s is View Serialisable.
	 * @param s
	 * @return
	 */
	public static boolean isVSR(Schedule s) {
		HashSet<ReadsFrom> rfs = ReadsFrom.createRF(s);
		
		for(Schedule serS : s.createSerialSchedules()){
			if(ReadsFrom.areSame(rfs, ReadsFrom.createRF(serS))){
				s.setSerialized(serS);
				return true;
			}
		}

		return false;
	}
}
