package compiler.lexer;

import java.util.*;

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
	
	GreaterThan(">"),
	GreaterThanEq(">="),
	LessThan("<"),
	LessThanEq("<="),
	
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

    private static List<Punctuator> sortedTokens;

    public static List<Punctuator> getSortedPunctuators() {
        if (sortedTokens != null) return sortedTokens;

        sortedTokens = Arrays.asList(Punctuator.values());
        Collections.sort(sortedTokens, TokenLengthComparator.Instance);

        return sortedTokens;
    }

    public static class TokenLengthComparator implements Comparator<Punctuator> {

        public static final TokenLengthComparator Instance = new TokenLengthComparator();

        @Override
        public int compare(Punctuator t1, Punctuator t2) {
            return t2.getString().length() - t1.getString().length();
        }

    }
}
