package test;

import junit.framework.Assert;

import main.JonLZW;

@SuppressWarnings("deprecation")

public class TestEncode extends LZWTestCase {

	public void testSingles() {
		Assert.assertEquals("00001", JonLZW.encode("A"));
		Assert.assertEquals("10110", JonLZW.encode("V"));
		Assert.assertEquals("01001", JonLZW.encode("I"));
	}

	public void testBadSingles() {
		Assert.assertEquals("", JonLZW.encode("a"));
		Assert.assertEquals("", JonLZW.encode(""));
		Assert.assertEquals("", JonLZW.encode(null));
	}

	public void testBadInput() {
		Assert.assertEquals("", JonLZW.encode("MaryHadALittleLamb"));
	}

	public void testExceedWordSize() {
		// Adds to dictionary as AA, AAA, AAAA, AAAAA, 
		//so the 15th A will give a word size of 5 if there is not a limit
		Assert.assertNotNull(JonLZW.encode("AAAAAAAAAAAAAAAA", 26, 31));
	}
	public void testNewDoubles() {
		// the last is Z, at 11010. So next encoding should be 11011.
		// Each encode gets a separate dictionary.
		Assert.assertEquals("0000110110", JonLZW.encode("AV"));
		Assert.assertEquals("1011000001", JonLZW.encode("VA"));
		Assert.assertEquals("0100110110", JonLZW.encode("IV"));
	}

	public void testToBe() {
		/*
		 * This is the example in wikipedia. String TOBEORNOTTOBEORTOBEORNOT The
		 * encode() range parameters allow us to check our dictionary internals
		 * against the wikipedia example
		 */
		LOGGER.info("TestEncode.testToBe() ");
		LOGGER.info("Besides checking the encoded value, this sends the interesting");
		LOGGER.info("part of the dictionary to console, to compare index values against the wikipedia example");

		LOGGER.info("encoding TOBEO, and check dictionary");
		Assert.assertEquals("1010001111000100010101111", JonLZW.encode("TOBEO", 26, 30));
		// it goes to 6-bit encoding after a few more
		LOGGER.info("encoding TOBEORNO, and check dictionary. Last value is 6-bit encoding");
		Assert.assertEquals("10100011110001000101011111001001110001111", JonLZW.encode("TOBEORNO", 26, 33));
		LOGGER.info("encoding TOBEORNOTTOBEORTOBEORNOT, and check dictionary");
		Assert.assertNotNull(JonLZW.encode("TOBEORNOTTOBEORTOBEORNOT", 26, 41));
	}

}
