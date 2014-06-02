package testing.tests.preprocessor;

import org.junit.Test;

import testing.TestHarness;

public class TrigraphTests {

	@Test
	public void trigraphTest1() {
		runTrigraphTest("trigraph_test_1");
	}

	
	private void runTrigraphTest(String baseTestName) {
		new TestHarness(new TrigraphTestRunner(), baseTestName + ".c", baseTestName + ".output").runTest();
	}
}
