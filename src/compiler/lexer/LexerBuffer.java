package compiler.lexer;

/**
 * Encapsulates the process of character/ string access to simplify the process of lexing.
 * 
 * @author troy
 */
public class LexerBuffer {
	
	private StringBuffer input;
	private int pos;

	public LexerBuffer(String text) {
		input = new StringBuffer(text);
	}
	
	public LexerBuffer(StringBuffer buffer) {
		input = buffer;
	}
	
	/**
	 * Returns true if we can match the given string from the current position in the buffer.
	 *
	 * This implies the buffer has enough characters left, and the characters actually match.
	 *
	 * @param toMatch the string to match q
	 * @return true if there is a match
	 */
	public boolean matches(String toMatch) {
		int index = input.indexOf(toMatch, pos);

		return index != -1 && index == pos;
	}
	
	/**
	 * Returns true if any of the given strings match. 
	 * Equivalent to calling the matches() function on each string individually, and
	 * returning the first true, or false otherwise.
	 * 
	 * @param toMatch
	 * @return
	 */
	public boolean matches(String ... toMatch) {
		for (String s : toMatch) {
			if (matches(s)) return true;
		}
		
		return false;
	}
	
	public boolean matches(CharType type) {
		return hasChar() && type.matches(peekChar());
	}
	
	/**
	 * Consumes the given string.
	 * 
	 * If the string is not directly present, an error is thrown.
	 * 
	 * @param toConsume
	 */
	public void consume(String toConsume) {
		if (!matches(toConsume)) throw new Error("error consuming: " + toConsume);
		
		pos += toConsume.length();
	}
	
	/**
	 * Attempts to consume the given string, consuming if present.
	 * If the string does not immediately match, this method does nothing.
	 * 
	 * @param toConsume
	 * @return true if the string matched, false otherwise
	 */
	public boolean tryConsume(String toConsume) {
		if (!matches(toConsume)) return false;
		
		consume(toConsume);
		
		return true;
	}
	
	/**
	 * Retrieves all characters in the buffer that match the given CharType 
	 * until a character is present that does not match, or the buffer is empty.
	 */
	public String nextMatching(CharType type) {
		StringBuffer buffer = new StringBuffer();
		
		while (type.matches(peekChar())) {
			buffer.append(consumeChar());
		}
		
		return buffer.toString();
	}
	
	/**
	 * Returns true if there is at least 1 more character remaining.
	 * 
	 * @return
	 */
	public boolean hasChar() {
		return pos < input.length();
	}
	
	/**
	 * Returns, but does not consume, the next character in the input.
	 * 
	 * @return
	 */
	public char peekChar() {
		return input.charAt(pos);
	}
	
	/**
	 * Moves the buffer back 1 character. Only valid if our current position is not the start.
	 */
	public void rewind() {
		if (pos == 0) {
			throw new IllegalStateException("Attempting to rewind past the start of the buffer");
		}
		
		pos--;
	}
	
	/**
	 * Returns true if the given number of characters are still left to be processed.
	 * 
	 * @param numChars
	 * @return
	 */
	public boolean hasNumChars(int numChars) {
		if (numChars < 1) throw new IllegalArgumentException("num chars must be 1 or greater");
		
		return (pos + numChars - 1) < input.length();
	}
	
	/**
	 * Consumes the next character, returning it as well.
	 * 
	 * @return
	 */
	public char consumeChar() {
		return input.charAt(pos++);
	}
	
	/**
	 * Skip over any whitespace at the current index position in the input string.
	 */
	public void skipWhitespace() {
		while (hasChar() && Character.isWhitespace(peekChar())) {
			pos++;
		}
	}
	
	/**
	 * Consumes single characters until a match of the given string occurs, which 
	 * is also consumed.
	 * 
	 * Throws an error if the given string isn't present in the remainder of the buffer.
	 * 
	 * @param toMatch
	 */
	public void consumeUntil(String toMatch) {
		while (hasChar()) {
			if (tryConsume(toMatch)) {
				return;
			} else {
				pos++;
			}
		}
		
		throw new Error("Attempt to consume until (" + toMatch + ") failed, EOF reached.");
	}
	
	/**
	 * Attempts to consume until the given string is reached.
	 * 
	 * If the string is found, the position now points to the first char beyond the found string,
	 * or to the EOF if it happens to be reached.
	 * 
	 * If the string is not found, the position is put back to the beginning.
	 * 
	 * @param toMatch
	 */
	public boolean tryConsumeUntil(String toMatch) {
		int positionBefore = pos;
		
		while (hasChar()) {
			if (tryConsume(toMatch)) {
				return true;
			}
			
			pos++;
		}
		
		pos = positionBefore;
		return false;
	}
	
	/**
	 * Returns a substring of the buffer at the given indexes.
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public String substring(int start, int end) {
		return input.substring(start, end);
	}
	
	/**
	 * Returns the character at the given index.
	 * 
	 * @param index
	 * @return
	 */
	public char charAt(int index) {
		return input.charAt(index);
	}
	
	/**
	 * Returns the current position into the buffer.
	 * 
	 * @return
	 */
	public int getPosition() {
		return pos;
	}
	
	/**
	 * Resets this LexerHelper, setting the internal position back to 0.
	 */
	public void resetPosition() {
		pos = 0;
	}
}
