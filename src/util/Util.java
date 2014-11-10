package util;

public class Util {

	/**
	 * Checks if the variable is null, throwing a NullPointerException if so.
	 * 
	 * @param o
	 */
	public static void nullCheck(Object o) {
		if (o == null) throw new NullPointerException();
	}
}
