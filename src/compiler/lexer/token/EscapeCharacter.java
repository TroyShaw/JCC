package compiler.lexer.token;

public enum EscapeCharacter {
	Tab("\\t", 't', '\t'),
	Newline("\\n", 'n', '\n'),
	Quote("\\\"", '"', '\"');
	
	private String text;
	private char charVal;
	private char escapedChar;
	
	EscapeCharacter(String text, char charVal, char escapedChar) {
		this.text = text;
		this.charVal = charVal;
		this.escapedChar = escapedChar;
	}

	public String getText() {
		return text;
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
	 * Returns the escaped char which includes the slash \.
	 * e.g. '\n', '\t', etc.
	 * @return
	 */
	public char getEscapedChar() {
		return escapedChar;
	}
}