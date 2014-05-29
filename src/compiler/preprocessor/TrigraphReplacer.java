package compiler.preprocessor;

import util.StringBufferUtil;

/**
 * Class handles the replacement of trigraphs within a C program.
 * 
 * @author troy
 */
public final class TrigraphReplacer {

	private TrigraphReplacer() {
		// Stop instantiation
	}
	
	/**
	 * Replaces all instances of the given trigraph to its single character representation.
	 * 
	 * @param toReplace
	 * @param trigraph
	 */
	public static void replaceSingleTrigraph(StringBuffer toReplace, Trigraph trigraph) {
		StringBufferUtil.replaceAll(toReplace, trigraph.getTrigraph(), trigraph.getSingleCharEquiv());
	}
	
	/**
	 * For each trigraph defined in Trigraph, its 3 character trigraph is replaced by
	 * the equivilant in the given StringBuffer.
	 * 
	 * @param toReplace
	 */
	public static void replaceAll(StringBuffer toReplace) {
		for (Trigraph t : Trigraph.values()) {
			replaceSingleTrigraph(toReplace, t);
		}
	}
}
