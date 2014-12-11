package compiler.lexer;

import compiler.lexer.token.CharacterToken;
import compiler.lexer.token.EscapeCharacter;
import compiler.lexer.token.FloatingToken;
import compiler.lexer.token.IdentifierToken;
import compiler.lexer.token.IntegerToken;
import compiler.lexer.token.KeywordToken;
import compiler.lexer.token.LiteralToken;
import compiler.lexer.token.PunctuatorToken;
import compiler.lexer.token.StringToken;
import compiler.lexer.token.Token;

import java.util.Arrays;
import java.util.List;

/**
 * Class provides helper methods to lex certain language elements.
 * 
 * To use this class, call one of the tryLex*() methods.
 * The method will attempt to lex the given token, returning true if it succeeds.
 * If a lex occurs, one should then call getToken() to access this saved lexed token.
 * 
 * @author troy
 */
public class LexerHelper {

	private Token lastLexedToken;
	
	private LexerBuffer b;
	
	public LexerHelper(LexerBuffer buffer) {
		this.b = buffer;
	}
	
	/**
	 * Returns the last lexed token.
	 * 
	 * @return
	 */
	public Token getToken() {
		return lastLexedToken;
	}
	
	/**
	 * Tries to lex a string literal, returning true if it succeeds.
	 * 
	 * @return
	 */
	public boolean tryLexStringLiteral() {
		if (!b.matches("\"", "L\"")) return false;

		boolean isWide = b.tryConsume("L");
		
		b.consume("\"");

		StringBuffer buffer = new StringBuffer();

		while (b.hasChar()) {
			char c = scanSimpleChar();

			if (c == '"') return setToken(new StringToken(buffer.toString(), isWide));

			buffer.append(c);
		}

		throw syntaxError("Reached EOF while parsing string literal");
	}
	
	/**
	 * Tries to lex a character literal, returning true if it succeeds.
	 * 
	 * @return
	 */
	public boolean tryLexCharLiteral() {
		if (!b.matches("\'", "L'")) return false;
		
		boolean isWide = b.tryConsume("L");
		
		b.consume("\'");
		
		StringBuffer buffer = new StringBuffer();
		
		while (b.hasChar()) {
			char c = scanSimpleChar();
			
			if (c == '\'') return setToken(new CharacterToken(buffer.toString(), isWide));
			
			buffer.append(c);
		}
		
		throw syntaxError("Reached EOF while parsing char literal");
	}
	
	/**
	 * Tries to lex a numerical literal, returning true if it succeeds.
	 * 
	 * @return
	 */
	public boolean tryLexNumericalLiteral() {
		return tryLexFloat() || tryLexInt();
	}

    /**
     * Tries to match against an integer constant.
     *
     * There are three main forms, decimal, octal, hexadecimal, all with an optional suffix.
     *
     * @return true if it matched, false otherwise
     */
    private boolean tryLexInt() {
        b.push();

        Token newInt = null;

        String rest;

        //first match the numerical part of the integer
        if (b.tryConsume("0x", "0X")) {
            rest = b.nextMatching(CharType.Hex);

            newInt = new IntegerToken(rest);
        } else if (b.matches("0")) {
            rest = b.nextMatching(CharType.Octal);

            newInt = new IntegerToken(rest);
        } else if (b.matches(CharType.Decimal)) {
            rest = b.nextMatching(CharType.Decimal);

            newInt = new IntegerToken(rest);
        } else {
            //none matched, not an integer constant
            b.pop();

            return false;
        }

        //now match the optional suffix
        String suffix = b.nextMatching(CharType.Character);

        List<String> validSuffixes = Arrays.asList("", "u", "U", "l", "L", "ll", "LL",
                "ul", "uL", "Ul", "UL",
                "ull", "uLL", "Ull", "ULL",
                "lu", "lU", "Lu", "LU",
                "llu", "llU", "LLu", "LLU");

        if (!validSuffixes.contains(suffix)) {
            //invalid floating point suffix
            throw new RuntimeException("invalid suffix \"" + suffix + "\" on integer constant");
        }

        return setToken(new IntegerToken(rest + suffix));
    }

    /**
     * Tries to lex a floating point literal.
     *
     * There are two forms of floating point constant, decimal and hexidecimal.
     *
     * @return true if it matched, false otherwise
     */
    private boolean tryLexFloat() {
        b.push();

        String result = "";

        String intPart = "";
        String floatPart = "";

        boolean requiresExponent = false;

        if (b.tryConsume("0x", "0X")) {
            //a hex floating constant
            b.pop();
            return false;
        } else {
            //a decimal floating constant
            intPart = b.nextMatching(CharType.Decimal);

            if (!intPart.isEmpty() && b.matches("e", "E")) {
                //we've got an exponent form float
                requiresExponent = true;
            } else if (b.tryConsume(".")) {
                //we've got a float in the form x.x
                floatPart = b.nextMatching(CharType.Decimal);
            } else {
                //we don't have a float
                b.pop();

                return false;
            }
        }

        int sign = 1;
        String exponent = "1";

        //exponent
        if (b.tryConsume("e", "E")) {

            if (b.tryConsume("-")) sign = -1;
            else if (b.tryConsume("+")) sign = 1;

            exponent = b.nextMatching(CharType.Decimal);

            if (exponent.isEmpty()) throw new RuntimeException("Exponent has no digits");

        } else if (requiresExponent) {
            // There was no dot, and we needed an exponent to be a floating.
            // Might be an integer though...
            b.pop();

            return false;
        }

        //optional floating suffix
        String suffix = b.nextMatching(CharType.Character);

        //verify it is correct
        List<String> validSuffixes = Arrays.asList("", "f", "F", "l", "L");

        if (!validSuffixes.contains(suffix)) {
            //invalid floating point suffix
            throw new RuntimeException("invalid suffix \"" + suffix + "\" on floating constant");
        }

        String signStr = sign == 1 ? "+" : "-";

        return setToken(new FloatingToken(intPart + "." + floatPart + "e" + signStr + exponent + suffix));
    }
	
	/**
	 * Tries to lex an identifier or keyword, returning true if this succeeds.
	 * 
	 * @return
	 */
	public boolean tryLexIdenOrKeyword() {
		if (!b.matches(CharType.IdentifierStart)) return false;
		
		String word = matchIdentifier();
		
		// Check if the word is a keyword
		for (Keyword k : Keyword.values()) {
			if (word.equals(k.getString())) {
				return setToken(new KeywordToken(k));
			}
		}
		
		//Otherwise it must be an identifier
		return setToken(new IdentifierToken(word));
	}
	
	/**
	 * Tries to lex a punctuator, returning true if it succeeds.
	 * 
	 * @return
	 */
	public boolean tryLexPunctuator() {
		for (Punctuator o : Punctuator.getSortedPunctuators()) {
			if (b.tryConsume(o.getString())) {
				return setToken(new PunctuatorToken(o));
			}
		}
		
		return false;
	}
	
	/**
	 * Scans tokens until a non-identifier token is found, returning the identifier
	 * formed in this process.
	 * 
	 * This method assumes at least 1 character is in the buffer that can start the
	 * identifier.
	 * 
	 * @return
	 */
	private String matchIdentifier() {
		return b.nextMatching(CharType.IdentifierRest);
	}
	
	 /**
	 * Scans the next char in the input, consuming and returning it.
	 * 
	 * Note: this isn't for scanning a char representation in source code, e.g. 'a'
	 * This simply scans a single char and returns it, e.g. a
	 * 
	 * Handles escape characters properly, consuming 2 chars for an escaped char.
	 * If EOF is reached, a syntax error is thrown.
	 * 
	 * @return
	 */
	private char scanSimpleChar() {
		char c = b.consumeChar();
		
		if (c != '\\') return c;
		
		//we've got an escaped char
		c = b.consumeChar();
		
		for (EscapeCharacter esChar : EscapeCharacter.values()) {
			if (c == esChar.getCharValue()) {
				return esChar.getEscapedChar();
			}
		}
		
		throw syntaxError("unrecognised escape character");
	}
	
	/**
	 * Scans a numerical literal.
	 * 
	 * @return
	 */
	private LiteralToken scanNumericLiteral() {
		String firstNum = scanNumeric();
		
		if (b.tryConsume(".")) {
			String secondNum = scanNumeric();
			
			return new FloatingToken(firstNum + "." + secondNum);
		} else {
			return new IntegerToken(firstNum);
		}
	}
	
	/**
	 * Scans from the current character until the last that isn't a number.
	 * These characters are concatenated and returned.
	 * 
	 * The lexer-helpers internal offset is moved by calling this function.
	 * 
	 * If no number can be made, an exception is thrown.
	 * 
	 * @return
	 */
	private String scanNumeric() {
		if (!b.hasChar()) throw syntaxError("Reached EOF while parsing numeric value");
		if (!Character.isDigit(b.peekChar())) throw syntaxError("Non-numeric character found instead of digit");
			
		StringBuffer buffer = new StringBuffer();
		
		while (Character.isDigit(b.peekChar())) buffer.append(b.consumeChar());
			
		return buffer.toString();
	}
	
	/**
	 * Scans a c integer constant.
	 * 
	 * @return
	 */
	private LiteralToken scanIntegerConstant() {
		if (b.tryConsume("0x") || b.tryConsume("0X")) {
			//hexidecimal constant
		} else if (b.tryConsume("0")) {
			//octal constant
		} else {
			//normal integer constant
		}
		
		return null;
	}	
	
	/**
	 * Scans and returns the next hex string.
	 * @return
	 */
	private String scanHexString() {
		String ox;
		
		if (b.tryConsume("0x")) ox = "0x";
		else if (b.tryConsume("0X")) ox = "0x";
		else throw syntaxError("invalid hex string");
		
		//now scan digits
		char c = b.peekChar();
		
		if (!CharType.Hex.matches(c)) {
			throw syntaxError("incomplete hex string");
		}
		
		return ox + scanHexNumber();
	}

	private String scanDecimalNumber() {
		return b.nextMatching(CharType.Decimal);
	}
	
	private String scanOctalNumber() {
		return b.nextMatching(CharType.Octal);
	}
	
	private String scanHexNumber() {
		return b.nextMatching(CharType.Hex);
	}
	
	/**
	 * Scans a single universal character name.
	 * 
	 * This assumes a valid UCN is currently present in the buffer.
	 * 
	 * @return
	 */
	private String scanUniversalCharacterName() {
		if (b.tryConsume("\\u")) {
			return "\\u" + consumeUCNHexQuad();
		} else if (b.tryConsume("\\U")) {
			return "\\U" + consumeUCNHexQuad() + consumeUCNHexQuad();
		} else {
			throw syntaxError("invalid UCN string");
		}
	}
	
	/**
	 * Consumes 4 characters which should valid hex characters.
	 * 
	 * @return a string of length 4 containing valid hex characters.
	 */
	private String consumeUCNHexQuad() {
		String toReturn = "";
		
		for (int i = 0; i < 4; i++) {
			char c = b.peekChar();
			
			if (!CharType.Hex.matches(c)) {
				throw syntaxError("UCN requires at least 4 hex digits");
			}
			
			toReturn += b.consumeChar();
		}
		
		return toReturn;
	}
	
	/**
	 * Returns true if the current buffer is the start of a numerical constant.
	 * 
	 * @return
	 */
	private boolean isNumericalStart() {
		// A numerical value either starts with a digit, or a dot followed by a digit
		if (b.matches(CharType.Decimal)) return true;
		
		if (b.peekChar() == '.') {
			// Check the next digit is numerical. Be sure the preserve the buffer state.
			b.consumeChar();
			char c = b.peekChar();
			b.rewind();
			
			return CharType.Decimal.matches(c);
		} else {
			return false;
		}
	}
	
	/**
	 * Private helper method which sets the internal token to the given
	 * value then returns true.
	 * 
	 * @param token
	 * @return
	 */
	private boolean setToken(Token token) {
		lastLexedToken = token;
		
		return true;
	}
	
	private RuntimeException syntaxError(String error) {
		return new RuntimeException(error);
	}
}
