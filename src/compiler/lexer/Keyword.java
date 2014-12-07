package compiler.lexer;

public enum Keyword {

	Auto("auto"),
	Break("break"),
	Case("case"),
	Char("char"),
	Const("const"),
	Continue("continue"),
	Default("default"),
	Do("do"),
	Double("double"),
	Else("else"),
	Enum("enum"),
	Extern("extern"),
	Float("float"),
	For("for"),
	Goto("goto"),
	If("if"),
	Inline("inline"),
	Int("int"),
	Long("long"),
	Register("register"),
	Restrict("restrict"),
	Return("return"),
	Short("short"),
	Signed("signed"),
	Sizeof("sizeof"),	//TODO: actually an operator, move somewhere else?
	Static("static"),
	Struct("struct"),
	Switch("switch"),
	Typedef("typedef"),
	Union("union"),
	Unsigned("unsigned"),
	Void("void"),
	Volatile("volatile"),
	While("while");

	private String str;
	
	Keyword(String str) {
		this.str = str;
	}
	
	public String getString() {
		return str;
	}
}
