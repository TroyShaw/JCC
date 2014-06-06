package compiler.lexer;

/**
 * Enum contains the different character types, and methods relating to checking
 * for that given type.
 * 
 * @author troy
 */
public enum CharType {
	Decimal {
		@Override
		public boolean matches(char c) {
			return '0' <= c && c <= '9';
		}
	},
	
	Octal {
		@Override
		public boolean matches(char c) {
			return '0' <= c && c <= '7';
		}
	},
	
	Hex {
		@Override
		public boolean matches(char c) {
			return Decimal.matches(c) || ('a' <= c && c <= 'f') || ('A' <= c && c <= 'F');
		}
	},
	
	IdentifierStart {
		@Override
		public boolean matches(char c) {
			return c == '_' || ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
		}
	},
	
	IdentifierRest {
		@Override
		public boolean matches(char c) {
			return IdentifierStart.matches(c) || Decimal.matches(c);
		}
	};
	
	/**
	 * Returns true if this character matches the given character type.
	 * 
	 * @param c
	 * @return
	 */
	public abstract boolean matches(char c);

}
