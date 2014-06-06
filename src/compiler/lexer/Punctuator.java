package compiler.lexer;

public enum Punctuator {
	// braces/ brackets
	LeftParenthesis("("),
	RightParenthesis(")"),
	LeftSquareBracket("["),
	RightSquareBracket("]"),
	LeftCurlyBracket("{"),
	RightCurlyBracket("}"),
	
	Colon(":"),
	SemiColon(";"),
	Comma(","),
	QuestionMark("?"),
	Ellipsis("..."),
	
	Dot("."),
	PointerDereference("->"),
	
	Plus("+"),
	Minus("-"),
	Asterisk("*"),
	Ampersand("&"),
	Slash("/"),
	Percent("%"),
	
	PlusPlus("++"),
	MinusMinus("--"),

	EqualEqual("=="),
	NotEqual("!="),
	
	GreaterThan("<"),
	GreaterThanEq("<="),
	LessThan(">"),
	LessThanEq(">="),
	
	LogicalNot("!"),
	LogicalAnd("&&"),
	LogicalOr("||"),
	
	LeftShift("<<"),
	RightShift(">>"),
	
	Tilde("~"),
	Caret("^"),
	Bar("|"),
	
	Equal("="),
	PlusAssign("+="),
	MinusAssign("-="),
	MultiplyAssign("*="),
	DivideAssign("/="),
	RemainderAssign("%="),
	LeftShiftAssign("<<="),
	RightShiftAssign(">>="),
	BitwiseAndAssign("&="),
	BitwiseOrAssign("|="),
	BitwiseXorAssign("^=");
	
	private String str;
	
	Punctuator(String str) {
		this.str = str;
	}
	
	public String getString() {
		return str;
	}
}
