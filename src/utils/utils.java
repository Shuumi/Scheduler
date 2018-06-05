package utils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class utils {

	// -----------------------permutations-------------------------

	/**
	 * See Steinhaus–Johnson–Trotter Algorithm for the explanation.
	 * Returns all permutations of the given array.
	 * @param array
	 * @return
	 */
	public static HashSet<Object[]> createPermutations(Object[] array) {
		if (array.length < 2) {
			HashSet<Object[]> returned = new HashSet<>();
			returned.add(array);
			return returned;
		}
		int arrLen = array.length;
		int maxIndex = arrLen - 2;
		int direction = -1;
		int numIters = factorial(arrLen);
		Object[] arrayCopy = array.clone();

		HashSet<Object[]> returned = new HashSet<>();
		int currIndex = maxIndex;
		int nextIndex = Math.max(0, maxIndex - 1);
		boolean skipIndex = false;
		boolean justSkipped = false;
		for (int i = 0; i < numIters; i++) {
			returned.add(arrayCopy);
			arrayCopy = swap(arrayCopy, currIndex, currIndex + 1);

			if (skipIndex) {
				currIndex = (currIndex == 0) ? maxIndex : 0;
				skipIndex = false;
				justSkipped = true;
			} else {
				currIndex = nextIndex;
				if (!justSkipped && (currIndex == maxIndex || currIndex == 0)) {
					skipIndex = true;
					direction *= -1;
				} else {
					nextIndex += direction;
					justSkipped = false;
				}
			}
		}

		return returned;
	}

	private static int factorial(int a) {
		int returned = 1;
		for (int i = 1; i <= a; i++) {
			returned *= i;
		}
		return returned;
	}

	private static Object[] swap(Object[] array, int index1, int index2) {
		Object[] returned = array.clone();

		Object ph = returned[index1];
		returned[index1] = returned[index2];
		returned[index2] = ph;

		return returned;
	}
}
