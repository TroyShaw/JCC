package util;

public class StringBufferUtil {

	/**
	 * Replaces the next instance of the given from string to the given to string.
	 * 
	 * If a string is replaced, this function returns true. 
	 * If from is not present in the buffer, this function returns false.
	 * 
	 * @param buffer
	 * @param from
	 * @param to
	 * @return
	 */
	public static boolean replace(StringBuffer buffer, String from, String to) {
		int index = buffer.indexOf(from);
		
		if (index == -1) return false;
		
		buffer.replace(index, index + from.length(), to);
		
		return true;
	}
	
	/**
	 * Replaces every instance of the given from string to the given to string in the
	 * given buffer.
	 * 
	 * This function returns true if 1 or more replacements are made, it returns 
	 * false otherwise.
	 * 
	 * @param buffer
	 * @param from
	 * @param to
	 * @return
	 */
	public static boolean replaceAll(StringBuffer buffer, String from, String to) {
		int index = buffer.indexOf(from);
		
		if (index == -1) return false;
		
		while (index != -1) {
			buffer.replace(index, index + from.length(), to);
			index += from.length();
			index = buffer.indexOf(from, index);
		}
		
		return true;
	}

}
