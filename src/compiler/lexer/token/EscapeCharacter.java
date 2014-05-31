package compiler.lexer.token;

/**
 * Class contains the default escape sequence set defined in the C standard.
 * 
 * @author troy
 */
public enum EscapeCharacter {
	Alarm         ("a",  'a',  (char) 0x07),
	Backspace     ("b",  'b',  (char) 0x08),
	Formfeed      ("f",  'f',  (char) 0x0C),
	Newline       ("n",  'n',  (char) 0x0A),
	CarriageReturn("r",  'r',  (char) 0x0D),
	HorizontalTab ("t",  't',  (char) 0x09),
	VerticalTab   ("v",  'v',  (char) 0x0B),
	QuestionMark  ("?",  '?',  (char) 0x3F),
	Backslash     ("\\", '\\', (char) 0x5C),
	SingleQuote   ("\'", '\'', (char) 0x27),
	DoubleQuote   ("\"", '\"', (char) 0x22);
	
	private String text;
	private char charVal;
	private char escapedChar;
	
	EscapeCharacter(String text, char charVal, char escapedChar) {
		this.text = text;
		this.charVal = charVal;
		this.escapedChar = escapedChar;
	}

	/**
	 * Returns a string representation of the entire value, including leading \.
	 * @return
	 */
	public String getText() {
		return "\\" + text;
	}
	
	/**
	 * Returns the real value in the escape.
	 * 
	 * E.g. for newline '\n', this returns 'n'.
	 * 
	 * @return
	 */
	public char getCharValue() {
		return charVal;
	}
	
	/**
	 * Returns the actual char code of the escape value.
	 * e.g. Newline returns 0x0A, etc.
	 * 
	 * @return
	 */
	public char getEscapedChar() {
		return escapedChar;
	}
}