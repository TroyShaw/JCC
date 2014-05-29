package testing;

import io.IOUtils;

import org.junit.Assert;


public class TestHarness {

	private TestRunner runner;
	
	private String inputFilename;
	private String expectedOutputFilename;
	
	/**
	 * Constructs a basic TestHarness.
	 * 
	 * @param runner
	 * @param inputFilename
	 * @param expectedOutputFilename
	 */
	public TestHarness(TestRunner runner, String inputFilename, String expectedOutputFilename) {
		this.runner = runner;
		this.inputFilename = inputFilename;
		this.expectedOutputFilename = expectedOutputFilename;
	}

	/**
	 * Executes the given test, calling code on the classes TestRunner, then comparing
	 * the generated output with the expected.
	 */
	public void runTest() {
		String programOutput = runner.run(getProgramInput());
		String expectedOutput = getExpectedOutput();
		
		//TODO make this compare the contents of the files
		Assert.assertEquals(expectedOutput, programOutput);
	}
	
	private String getProgramInput() {
		return IOUtils.readFile(inputFilename);
	}
	
	private String getExpectedOutput() {
		return IOUtils.readFile(expectedOutputFilename);
	}
}
