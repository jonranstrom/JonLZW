package test;

import junit.framework.TestCase;

abstract public class LZWTestCase extends TestCase {
	/**
	 * Should use log4j. Staying simple.
	 */
	static class LOGGER {
		static void info(String input) {
			System.out.println("INFO: " + input);
		}
	}
}
