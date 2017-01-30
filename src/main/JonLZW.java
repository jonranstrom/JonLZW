package main;

/**
 * LZW encoder/decoder prepared for Eric Dimaano and SigFig Jan. 27 - 29, 2017
 * 
 * @author jonranstrom
 *
 */
public class JonLZW {

	public static void main(String[] args) {
		boolean err = false;
		if (args.length < 2) {
			err = true;
		}
		if (!err && args[1].isEmpty()) {
			err = true;
		}
		if (!err) {
			String charStr = args[0];
			if (charStr.equals("+")) {
				System.out.println(encode(args[1]));
			}
			if (charStr.equals("-")) {
				System.out.println(decode(args[1]));
			}
			/*
			 * if (charStr.equals("*")) { System.out.println("testing " +
			 * args[1]); if (decode(encode(args[1]) .equals(args[1])
			 * System.out.println("passed"); else System.out.println("failed");
			 * 
			 * }
			 */
		}

		if (err) {
			System.out.println("Pass args as + string or - string or * string");
			System.out.println("+ ABC  will encode ABC. Only capitals letters work.");
			System.out.println("- 000101  will decode. Pass the string from encode.");
			System.out.println("* ABC  will encode, then decode. It returns if it matched.");
		}
	}

	/**
	 * Or constructor Useful for unit tests
	 */
	public void JonLWZ() {

	}

	/**
	 * The normal encode() API
	 */
	public static String encode(String allCapsString) {
		return encode(allCapsString, 0, 0); // no special logging
	}

	/**
	 * 
	 * Basic LZW encoder. Extra parameters report range of indices in the
	 * dictionary
	 */
	public static String encode(String allCapsString, int firstIndex, int lastIndex) {
		/*
		 * Psuedo-code s is empty string while chars remain { take a character
		 * if dictionary contains s + char s = s + char else encode s to output
		 * add s + char to dictionary s = char } encode remaining s to output
		 * 
		 */
		int curentCodeWidth = 5;
		int newCodeWidth = 5;
		String newCode = "";

		DictionaryLZW dict = new DictionaryLZW();
		// check that the input string is clean
		if (!dict.checkEntry(allCapsString)) {
			return "";
			// ERROR
		}

		String returnStr = "";
		String integersStr = "";

		String s = "";
		for (char c : allCapsString.toCharArray()) {
			// word size limit check here?

			String testString = s + c;
			if (dict.findEntry(testString) > 0) {
				s = testString;
			} else {
				newCode = toOutput(dict, s, curentCodeWidth);
				returnStr = returnStr.concat(newCode);
				integersStr = LZWLog.appendIntegerIfLogging(integersStr, newCode, firstIndex, curentCodeWidth);

				// location for increment after next write
				if (newCodeWidth != curentCodeWidth) {
					curentCodeWidth = newCodeWidth;
				}

				newCodeWidth = dict.addEntryGetCodeWidth(testString, curentCodeWidth);
				// The code size may have changed on last dictionary add.
				// But change after the next read

				s = String.valueOf(c);
			}

		}
		newCode = toOutput(dict, s, curentCodeWidth);
		returnStr = returnStr.concat(newCode);
		integersStr = LZWLog.appendIntegerIfLogging(integersStr, newCode, firstIndex, curentCodeWidth);

		// debugging aid if indices are set
		LZWLog.logDebugStrings(returnStr, integersStr, firstIndex);
		LZWLog.logDictionary(dict, firstIndex, lastIndex);

		return returnStr;
	}

	/**
	 * Helper function called in two places in encode()
	 */
	private static String toOutput(DictionaryLZW dict, String inStr, int minimumLength) {
		int index = dict.findEntry(inStr);
		String str = BinaryString.toString(index, minimumLength);
		return str;
	}

	/**
	 * The normal decode() API
	 */
	public static String decode(String zeroesAndOnesString) {
		return decode(zeroesAndOnesString, 0, 0); // no special logging
	}

	/**
	 * 
	 * Basic LZW decoder. Extra parameters report range of indices in the
	 * dictionary
	 */
	public static String decode(String zeroesAndOnesString, int firstIndex, int lastIndex) {
		// Psuedo-code . Using the terms
		// char01 for character from the zeroesAndOnesString
		// code01 for sequence of char01 of the right length.
		// and word for decoded string of the code01

		// output is empty string or buffer
		// Dictionary starts at 5. Therefore:
		// previousCode01 = read five char01
		// previousWord = translate(previousCode01)
		// concat previousWord to output.
		//
		// while chars remain {
		// nextCode01 = read correct number of char01
		// index = dictionary index of nextCode01
		// word = translate(nextCode01)
		// concat word to output.
		// char = first character of word
		// add to dictionary previousWord + char
		//
		// previousWord = word, etc.
		// }
		//
		//

		DictionaryLZW dict = new DictionaryLZW();
		// Not checking that the input string is clean. Any invalid character is
		// interpretted as 0
		int currentCodeWidth = dict.getStartingCodeSize();

		String outStr = "";
		String integersStr = "";

		// InputZeroAndOneChars is a slight abstraction, that could be applied
		// to a stream
		int readCharCounter = 0;
		InputZeroAndOneChars input = new InputZeroAndOneChars(zeroesAndOnesString);
		int maxCharCounter = input.size();

		String previousCode = input.getCodeString(readCharCounter, currentCodeWidth);
		readCharCounter += currentCodeWidth;
		int codeIndex = BinaryString.toInt(previousCode);
		if (!dict.indexExists(codeIndex)) {
			return outStr; // first letter read is not a cap. Bail
		}

		String previousWord = dict.getStringAt(codeIndex);
		char firstChar = previousWord.charAt(0);
		outStr = outStr.concat(previousWord);
		integersStr = LZWLog.appendIntegerIfLogging(integersStr, codeIndex, firstIndex, currentCodeWidth);

		int newCodeWidth = currentCodeWidth;
		while (readCharCounter < maxCharCounter) {
			String currentCode = input.getCodeString(readCharCounter, currentCodeWidth);
			// if input had incomplete word, it will come from the getter as
			// empty string.
			if (currentCode.isEmpty()) {
				// This does not happen for valid cases. Only for leftover bits
				break;
			}
			readCharCounter += currentCodeWidth;

			codeIndex = BinaryString.toInt(currentCode);

			String currentWord = "";
			// Does this index exist in dictionary already?
			if (!dict.indexExists(codeIndex)) {
				currentWord = previousWord.concat(String.valueOf(firstChar));
				integersStr = LZWLog.appendGuessIfLogging(integersStr, currentWord, firstIndex, currentCodeWidth);
			} else {
				currentWord = dict.getStringAt(codeIndex);
				integersStr = LZWLog.appendIntegerIfLogging(integersStr, codeIndex, firstIndex, currentCodeWidth);
			}
			outStr = outStr.concat(currentWord);

			firstChar = currentWord.charAt(0);
			String newWordForDict = previousWord.concat(String.valueOf(firstChar));
			newCodeWidth = dict.addEntryGetCodeWidth(newWordForDict, currentCodeWidth);
			// The code size might have changed with the last dictionary add.
			// But change after the next read

			// location for increment before next read
			if (newCodeWidth != currentCodeWidth) {
				currentCodeWidth = newCodeWidth;
			}

			previousWord = currentWord;
		}

		// debugging aid if indices are passed in
		LZWLog.logDebugStrings(outStr, integersStr, firstIndex);
		LZWLog.logDictionary(dict, firstIndex, lastIndex);

		return outStr;
	}

}
