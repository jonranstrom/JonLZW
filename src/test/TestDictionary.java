package test;

import junit.framework.Assert;

import main.DictionaryLZW;

@SuppressWarnings("deprecation")


public class TestDictionary extends LZWTestCase {

	/**
	 * Check that dictionary constructor leaves it in expected starting state.
	 */
	public void testInit() throws Exception {
		LOGGER.info("TestDictionay.testInit(). Expect two console ERRORs");
		DictionaryLZW dict = new DictionaryLZW();
		// We should find "A" at 0 and "Z" at 25.
		Assert.assertEquals(1, dict.findEntry("A"));
		Assert.assertEquals(26, dict.findEntry("Z"));
		// check some that should not be found
		Assert.assertEquals(-1, dict.findEntry(""));
		Assert.assertEquals(-1, dict.findEntry("ZZ"));
		// and a null for robustness
		Assert.assertEquals(-1, dict.findEntry(null));
	}

	/**
	 * Test of adding to dictionary
	 */
	public void testAddEntry() throws Exception {
		LOGGER.info("TestDictionay.testAddEntry(). Expect seven console ERRORs");
		DictionaryLZW dict = new DictionaryLZW();

		// Test adding something that is already there. Should not happen.
		Assert.assertEquals(26, dict.size());
		dict.addEntry("T");
		Assert.assertEquals(26, dict.size());

		// Test adding bad input.
		dict.addEntry("*");
		Assert.assertEquals(26, dict.size());
		dict.addEntry("0");
		Assert.assertEquals(26, dict.size());
		dict.addEntry("a");
		Assert.assertEquals(26, dict.size());
		dict.addEntry("");
		Assert.assertEquals(26, dict.size());
		dict.addEntry(null);
		Assert.assertEquals(26, dict.size());
		dict.addEntry("a T");
		Assert.assertEquals(26, dict.size());

		// add a couple of valid ones.
		dict.addEntry("AT");
		Assert.assertEquals(27, dict.size());
		dict.addEntry("AA");
		Assert.assertEquals(27, dict.findEntry("AT"));
		Assert.assertEquals(28, dict.findEntry("AA"));
	}

	/**
	 * Test of fetching strings from dictionary
	 */
	public void testGet() throws Exception {
		LOGGER.info("TestDictionay.testGet()");
		DictionaryLZW dict = new DictionaryLZW();
		dict.addEntry("AT");
		dict.addEntry("AA");
		for (int i = 1; i < dict.size(); i++) {
			String val = dict.getStringAt(i);
			Assert.assertEquals(i, dict.findEntry(val));
		}
		// Now some bad attempts. Boundary errors will throw, so catch them here
		// and assert if there was not a throw.
		// This test might need maintenance as the dictionary data structure changes.
		// IndexOutOfBoundsException is fairly general, so it may work still.
		boolean caught = false;
		try {
			dict.getStringAt(-1);
		} catch (IndexOutOfBoundsException e) {
			caught = true;
		}
		Assert.assertTrue(caught);
		caught = false;
		try {
			dict.getStringAt(29);
		} catch (IndexOutOfBoundsException e) {
			caught = true;
		}
		Assert.assertTrue(caught);

	}

}
