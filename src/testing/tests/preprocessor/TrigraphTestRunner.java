package testing.tests.preprocessor;

import compiler.preprocessor.TrigraphReplacer;

import testing.TestRunner;

/**
 * Class implements TestRunner, simply calling the trigraph replacer on the
 * given code.
 * 
 * @author troy
 */
public class TrigraphTestRunner implements TestRunner {

	@Override
	public String run(String programInput) {
		StringBuffer buffer = new StringBuffer(programInput);
		
		TrigraphReplacer.replaceAll(buffer);
		
		return buffer.toString();
	}

}
