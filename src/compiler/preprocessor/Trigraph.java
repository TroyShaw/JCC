package compiler.preprocessor;

/**
 * Represents the various trigraph replacement strings.
 * 
 * @author troy
 */
public enum Trigraph {
	Hash       ("??=", "#"),
	Backslash  ("??/", "\\"),
	Caret      ("??'", "^"),
	LeftSquare ("??(", "["),
	RightSquare("??)", "]"),
	LeftCurly  ("??<", "{"),
	RightCurly ("??>", "}"),
	Bar        ("??!", "|"),
	Tidle      ("??-", "~");
	
	private String trigraph;
	private String singleCharEquiv;
	
	Trigraph(String trigraph, String singleCharEquiv) {
		this.trigraph = trigraph;
		this.singleCharEquiv = singleCharEquiv;
	}
	
	/**
	 * Returns the 3 character trigraph which should be replaced.
	 * 
	 * @return
	 */
	public String getTrigraph() {
		return trigraph;
	}
	
	/**
	 * Returns the 1 character replacement associated with the given
	 * trigraph.
	 * 
	 * @return
	 */
	public String getSingleCharEquiv() {
		return singleCharEquiv;
	}
}
