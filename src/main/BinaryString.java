package main;

public class BinaryString {

	/**
	 * Will assume ok without checking the string is valid A string of "0" and
	 * "1" such as "01100110" (bad characters interpreted as '0')
	 */
	static public int toInt(String input) {
		int returnValue = 0;
		for (int j = 0; j < input.length(); j++) {
			returnValue *= 2;
			returnValue += input.charAt(j) == '1' ? 1 : 0;
		}
		return returnValue;
	}

	/**
	 * Return minimum characters left padded with zeros. Longer if necessary.
	 */
	static public String toString(int input, int minimumLength) {
		String returnStr = Integer.toBinaryString(input);
		if (returnStr.length() < minimumLength) {
			// hack in place of StringUtils.leftPad()
			// ineffiecent. StringBuilder and new string on each iteration.
			while (returnStr.length() < minimumLength)
				returnStr = "0" + returnStr;
		}
		return returnStr;
	}
}
