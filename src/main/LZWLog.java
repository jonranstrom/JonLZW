package main;

/**
 * Helper functions for LZW Encode and decode. For JUnit tests, encode and
 * decode may have first and last index parameters. If so, there is helpful
 * logging. This class assists with that, avoiding some clutter in encode and
 * decode functions.
 * 
 * Three functions take a string, and add to it if firstIndex > 0. The last
 * function shows the structure of the dictionary.
 * 
 */
public class LZWLog {

	/**
	 * During decode, if a code not in the dictionary is found, append the
	 * guessed at word to the string instead of writing the integer. Also
	 * indicate the code width for that read.
	 */
	static String appendGuessIfLogging(String integersStr, String capsGuess, int firstIndex, int codeWidth) {
		if (firstIndex > 0) {
			integersStr = integersStr.concat(capsGuess + "-" + Integer.valueOf(codeWidth) + " ");
		}
		return integersStr;
	}

	/**
	 * During encode or decode, append integer value of the code if it is
	 * already in the dictionary, or is being added.
	 */
	static String appendIntegerIfLogging(String integersStr, String newCode, int firstIndex, int codeWidth) {
		if (firstIndex > 0) {
			Integer codeValue = BinaryString.toInt(newCode);
			integersStr = integersStr.concat(codeValue.toString() + "-" + Integer.valueOf(codeWidth) + " ");
		}
		return integersStr;
	}

	/**
	 * During encode or decode, append integer value if it is already in the
	 * dictionary, or is being added.
	 */
	static String appendIntegerIfLogging(String integersStr, int index, int firstIndex, int codeWidth) {
		if (firstIndex > 0) {
			Integer codeValue = index;
			integersStr = integersStr.concat(codeValue.toString() + "-" + Integer.valueOf(codeWidth) + " ");
		}
		return integersStr;
	}

	static void logDebugStrings(String returnStr, String integersStr, int firstIndex) {
		if (firstIndex > 0) {
			System.out.println(returnStr);
			System.out.println(integersStr);
		}
	}

	/**
	 * Helper function optionally called by encode and decode This will write
	 * dictionary contents to the console.
	 */
	static void logDictionary(DictionaryLZW dict, int firstIndex, int lastIndex) {
		// debugging aid if indices are set
		if (firstIndex <= lastIndex && firstIndex > 0) {
			int theSize = dict.size();
			System.out.println("Dictionary size is " + Integer.toString(theSize));
			if (lastIndex <= theSize) {
				for (int n = firstIndex; n <= lastIndex; n++) {
					String toLog = Integer.toString(n) + ": " + dict.getStringAt(n);
					System.out.println(toLog);
				}
			}
		}
	}

}
