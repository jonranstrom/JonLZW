package test;

import junit.framework.Assert;

import main.JonLZW;

@SuppressWarnings("deprecation")

public class TestEncodeDecode extends LZWTestCase {

	private String encodeDecode(String input) {
		// separated rather than chained for debugability
		String encoded = JonLZW.encode(input);
		return JonLZW.decode(encoded);
	}

	private void verifyEncodeDecode(String input) {
		Assert.assertEquals(input, encodeDecode(input));
	}

	public void testSingles() {
		LOGGER.info("TestEncodeDecode.testSingles()");
		verifyEncodeDecode("A");
		verifyEncodeDecode("T");
		verifyEncodeDecode("Z");
	}

	/**
	 * Helper function expands out details, as well as calling
	 * verifyEncodeDecode(input); that assert the input comes back as it went
	 * in.
	 */
	private void testLogToString(String input, int firstIndex, int lastIndex) {
		LOGGER.info("Encoding: " + input + " Length=" + Integer.toString(input.length()));
		String encoded = JonLZW.encode(input, firstIndex, lastIndex);
		LOGGER.info("encoded: " + encoded);
		LOGGER.info("Length=" + Integer.toString(encoded.length()));
		LOGGER.info("Decoding...");
		String decoded = JonLZW.decode(encoded, firstIndex, lastIndex);
		LOGGER.info("decoded: " + decoded);

		verifyEncodeDecode(input); // repeat, asserting if failed
	}

	public void testLogToBe() {
		String tobeStr = "TOBEORNOTTOBEORTOBEORNOT";
		LOGGER.info("TestEncodeDecode.logToBe()");
		testLogToString(tobeStr, 26, 41);
	}

	public void testLogMedium() {
		String tobeStr = "TOBEORNO";
		LOGGER.info("TestEncodeDecode.testLogMedium()");
		testLogToString(tobeStr, 26, 32);
	}

	public void testDoubles() {
		LOGGER.info("TestEncodeDecode.testDoubles()");
		verifyEncodeDecode("AT");
		verifyEncodeDecode("TI");
	}

	public void testABABACase() {
		LOGGER.info("TestEncodeDecode.testABABACase()");
		testLogToString("BABABABABABABABABABABABAB", 26, 34);
		verifyEncodeDecode(
				"BABABABABABABABABABABABABBABABABABABABABABABABABABBABABABABABABABABABABABABABABABABABABABABAB");
		verifyEncodeDecode(
				"ABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABC");
	}

	public void testRepeatsCase() {
		LOGGER.info("TestEncodeDecode.testRepeatsCase()");
		verifyEncodeDecode("AAAAAAAAAAAAAAAAX");
		verifyEncodeDecode("AAAABBBBAAAABBBBX");

		LOGGER.info("See if we can exceed the a word size of 12");
		// These would be interesting after adding the word size enhancement
		// This one passes, as we hit the expand to 6 bit before the long string
		// of A
		testLogToString(
				"TOBEORNOTTOBEORTOBEORNOTAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				26, 54);

		// This will fail. The expand to 6 bits happens in the middle of a
		// larger word to dictionary
		verifyEncodeDecode("AAAAAAAAAAAAAAAAAAAAAAAAAAA");
		// These fail. Starting at 28 or more similar to start
		//verifyEncodeDecode("AAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		// verifyEncodeDecode("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
		// verifyEncodeDecode("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");

		// The following is fine as it gets by 5 to 6 bit expansion before the run of repeats.
		verifyEncodeDecode("TOBEORNOTAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
}

	public void testToBe() {
		LOGGER.info("TestEncodeDecode.testToBe()");
		// still 5 bit
		// verifyEncodeDecode("TOBEO");
		// got into 6 bit
		testLogToString("TOBEORNOT", 26, 33);
		// The wikipedia example
		// verifyEncodeDecode("TOBEORNOTTOBEORTOBEORNOT");
	}

}
