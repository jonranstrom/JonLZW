package test;

import junit.framework.Assert;

import main.JonLZW;

@SuppressWarnings("deprecation")

public class TestDecode extends LZWTestCase {

	public void testSingles() {
		Assert.assertEquals("A", JonLZW.decode("00001"));
		Assert.assertEquals("V", JonLZW.decode("10110"));
		Assert.assertEquals("I", JonLZW.decode("01001"));
	}

	public void testNewDoubles() {
		// the last is Z, at 11010. So next encoding should be 11011.
		// Each decode gets a separate dictionary.
		LOGGER.info("TestDecode.testNewDoubles()");
		Assert.assertEquals("AV", JonLZW.decode("0000110110", 26, 27));
		Assert.assertEquals("VA", JonLZW.decode("1011000001", 26, 27));
		Assert.assertEquals("IV", JonLZW.decode("0100110110", 26, 27));
	}

	public void testBadInput() {
		Assert.assertEquals("B", JonLZW.decode("0001a")); // bad character
															// interpreted as
															// '0'
		Assert.assertEquals("", JonLZW.decode("0001")); // too short. Not even
		Assert.assertEquals("", JonLZW.decode("11111")); // Not valid for the
															// state of the
															// dictionary
		Assert.assertEquals("", JonLZW.decode("1111100001")); // Bad, then valid
																// A
		Assert.assertEquals("", JonLZW.decode(null));
		Assert.assertEquals("D", JonLZW.decode("0010011")); // Second code
															// incomplete, but
															// decode first
	}

	public void testToBePartial() {
		/*
		 * This is the example in wikipedia. String TOBEORNOTTOBEORTOBEORNOT The
		 * encode() range parameters allow us to check our dictionary internals
		 * against the wikipedia example
		 */
		LOGGER.info("TestDecode.testToBePartial() ");
		LOGGER.info("Besides checking the decoded value, this sends the interesting");
		LOGGER.info("part of the dictionary to console, to compare index values against the wikipedia example");

		Assert.assertEquals("TOBEO",JonLZW.decode("1010001111000100010101111"));
		
		// it goes to 6-bit encoding after a few more
		LOGGER.info("decode the encoded  TOBEORNO, and check dictionary");
		LOGGER.info("Notice the 15-6 as the last encoding. 'O' went as 6-bit encoding");

		// Let's get the string from encode
		String encoded = JonLZW.encode("TOBEORNO", 26, 33);
		Assert.assertEquals("TOBEORNO", JonLZW.decode("10100011110001000101011111001001110001111", 26, 33));
	}

}
