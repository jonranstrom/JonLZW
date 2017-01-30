package main;

/**
 * Very slight generalization from pure string implementation. The idea is this
 * could be adapted to a stream.
 * 
 * @author jonranstrom
 */
public class InputZeroAndOneChars {
	private char[] charsToRead;
	private int maxChars;

	public InputZeroAndOneChars(String input) {
		if (input == null) {
			maxChars = 0;
		} else {
			charsToRead = input.toCharArray();
			maxChars = charsToRead.length;
		}
	}

	public String getCodeString(int startOffset, int codeLength) {
		if (startOffset + codeLength > maxChars) {
			return ""; // return nothing for incomplete code
		}
		String outString = "";
		for (int n = startOffset; n < startOffset + codeLength; n++) {
			outString = outString.concat(String.valueOf(charsToRead[n]));
		}
		return outString;
	}

	public int size() {
		return maxChars;
	}
}
