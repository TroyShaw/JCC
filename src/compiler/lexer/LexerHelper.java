package compiler.lexer;

import compiler.lexer.token.*;
import compiler.lexer.token.cString.CCharSequence;

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
	 * Each lexing method either returns true or false.
     * If the method returns true, it means a lex was successful, and
     * this method should be called to get the successfully lexed token.
     *
     * This method will always return the last successfully lexed token, so calling after
     * an unsuccessful lex will still return that same last token.
     *
     * This method will return null until a successful lex happens.
	 * 
	 * @return the last lexed token.
	 */
	public Token getToken() {
		return lastLexedToken;
	}
	
	/**
	 * Tries to lex a string literal, returning true if it succeeds.
	 * 
	 * @return true if successful
	 */
	public boolean tryLexStringLiteral() {
		if (!b.matches("\"", "L\"")) return false;

        CCharSequence sequence = parseCharacterString('"');

        return setToken(new StringToken(sequence));
	}

	/**
	 * Tries to lex a character literal, returning true if it succeeds.
	 * 
	 * @return true if successful
	 */
	public boolean tryLexCharLiteral() {
        if (!b.matches("'", "L'")) return false;

        CCharSequence sequence = parseCharacterString('\'');

        if (sequence.length() == 0) {
            throw syntaxError("empty character literal");
        }

        return setToken(new CharacterToken(sequence));
	}

    private CCharSequence parseCharacterString(char delimiter) {
        boolean isWide = b.tryConsume("L");

        b.consume(delimiter);

        CCharSequence sequence = new CCharSequence(isWide);

        while (b.hasChar()) {
            char c = scanSimpleChar();

            if (c == '\n') {
                throw syntaxError("expected terminating " + delimiter);
            }

            if (c == delimiter) {
                return sequence;
            }

            sequence.addChar(c);
        }

        throw syntaxError("expected terminating " + delimiter);
    }
	
	/**
	 * Tries to lex a numerical literal, returning true if it succeeds.
	 * 
	 * @return true if successful
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

        String intString;
        NumericType numericType;

        //first match the numerical part of the integer
        if (b.tryConsume("0x", "0X")) {
            intString = b.nextMatching(CharType.Hex);
            numericType = NumericType.Hexidecimal;

            if (intString.length() == 0) {
                throw syntaxError("empty hex int constant");
            }
        } else if (b.matches("0")) {
            intString = b.nextMatching(CharType.Octal);
            numericType = NumericType.Octal;
        } else if (b.matches(CharType.Decimal)) {
            intString = b.nextMatching(CharType.Decimal);
            numericType = NumericType.Decimal;
        } else {
            //none matched, not an integer constant
            b.pop();

            return false;
        }

        //now match the optional suffix
        String suffix = b.nextMatching(CharType.Character);

        List<String> validSuffixes = Arrays.asList(
                "", "u", "U", "l", "L", "ll", "LL",
                "ul", "uL", "Ul", "UL",
                "ull", "uLL", "Ull", "ULL",
                "lu", "lU", "Lu", "LU",
                "llu", "llU", "LLu", "LLU");

        if (!validSuffixes.contains(suffix)) {
            //invalid floating point suffix
            throw new RuntimeException("invalid suffix \"" + suffix + "\" on integer constant");
        }

        return setToken(new IntegerToken(intString, suffix, numericType));
    }

    /**
     * Tries to lex a floating point literal.
     *
     * There are two forms of floating point constant, decimal and hexadecimal.
     *
     * @return true if it matched, false otherwise
     */
    private boolean tryLexFloat() {
        b.push();

        String intPart;
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

        return setToken(new FloatingToken(intPart, floatPart, sign, exponent, suffix, NumericType.Decimal));
    }
	
	/**
	 * Tries to lex an identifier or keyword, returning true if this succeeds.
	 * 
	 * @return true if an identifier or keyword is lexed, false otherwise
	 */
	public boolean tryLexIdenOrKeyword() {
		if (!b.matches(CharType.IdentifierStart)) return false;
		
		String word = b.nextMatching(CharType.IdentifierRest);
		
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
	 * @return true if a punctuator is lexed, false otherwise
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
	 * Scans the next char in the input, consuming and returning it.
	 * 
	 * Note: this isn't for scanning a char representation in source code, e.g. 'a'
	 * This simply scans a single char and returns it, e.g. a
	 * 
	 * Handles escape characters properly, consuming 2 chars for an escaped char.
	 * If EOF is reached, a syntax error is thrown.
	 * 
	 * @return a simple char
	 */
	private char scanSimpleChar() {
		char c = b.consumeChar();
		
		if (c != '\\') return c;

        // Do hex constant. This is \x[hex]+
        if (b.tryConsume("x")) {
            String hex = b.nextMatching(CharType.Hex);

            if (hex.length() == 0) {
                throw syntaxError("\\x used with no following hex digits");
            }

            return (char) hex.hashCode();
        }

        // Do octal constant. This is a sequence of 1, 2 or 3 octal digits
        if (b.matches(CharType.Octal)) {
            String chars = "";

            for (int i = 0; i < 3; i++) {
                if (!b.matches(CharType.Octal)) break;

                chars += b.consumeChar();
            }

            return (char) chars.hashCode();
        }

        // Do universal character name
        if (b.matches("u", "U")) {
            String ucn = scanUniversalCharacterName();

            return (char) ucn.hashCode();
        }

		// Do standard escape sequences
		c = b.consumeChar();
		
		for (EscapeCharacter esChar : EscapeCharacter.values()) {
			if (c == esChar.getCharValue()) {
				return esChar.getEscapedChar();
			}
		}

		throw syntaxError("unrecognised escape character: " + c);
	}

	/**
	 * Scans a single universal character name.
	 * 
	 * This assumes a valid UCN is currently present in the buffer.
	 * 
	 * @return a scanned universal character name
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
	 * Private helper method which sets the internal token to the given
	 * value then returns true.
	 * 
	 * @param token the token to set
	 * @return true always
	 */
	private boolean setToken(Token token) {
		lastLexedToken = token;
		
		return true;
	}
	
	private RuntimeException syntaxError(String error) {
		return new RuntimeException(error);
	}
}
