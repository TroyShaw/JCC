package compiler.lexer;

import compiler.lexer.token.EscapeCharacter;

/**
 * Enum contains the different character types, and methods relating to checking
 * for that given type.
 * 
 * @author troy
 */
public enum CharType {
	/**
	 * A decimal is defined as any character between 0-9.
	 */
	Decimal {
		@Override
		public boolean matches(char c) {
			return '0' <= c && c <= '9';
		}
	},
	
	/**
	 * An octal is defined as any character between 0-7.
	 */
	Octal {
		@Override
		public boolean matches(char c) {
			return '0' <= c && c <= '7';
		}
	},
	
	/**
	 * A hex is defined as any decimal character, as well as characters a-F and A-F.
	 */
	Hex {
		@Override
		public boolean matches(char c) {
			return Decimal.matches(c) || ('a' <= c && c <= 'f') || ('A' <= c && c <= 'F');
		}
	},
	
	/**
	 * An identifier start (the first character of an identifier) is defined as
	 * any of the underscore (_), a-z and A-Z.
	 */
	IdentifierStart {
		@Override
		public boolean matches(char c) {
			return c == '_' || ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
		}
	},
	
	/**
	 * An identifier rest (the characters after the first of an identifier) is
	 * defined as any identifier start character, as well as any decimal.
	 */
	IdentifierRest {
		@Override
		public boolean matches(char c) {
			return IdentifierStart.matches(c) || Decimal.matches(c);
		}
	},

    /**
     * A character is any of a-z or A-Z.
     */
    Character {
        @Override
        public boolean matches(char c) {
            return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
        }
    },

	/**
	 * Whitespace is defined as:
	 *  - space character
	 *  - newline character
	 *  - form-feed character
	 *  - horizontal-tab character 
	 *  - vertical-tab character
	 */
	WhiteSpace {
		@Override
		public boolean matches(char c) {
			// Have to special-case vertical-tab since Java doesn't have
			// an escape sequence for it.
			char vertChar = EscapeCharacter.VerticalTab.getCharValue();
			
			return c == ' ' || c == '\n' || c == '\f' || c == '\t' || c == vertChar;
		}
	};
	
	/**
	 * Returns true if this character matches the given character type.
	 * 
	 * @param c the character to match
	 * @return true if there is a match, false otherwise
	 */
	public abstract boolean matches(char c);
}
