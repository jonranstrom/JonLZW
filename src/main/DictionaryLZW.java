package main;

import java.util.ArrayList;

/**
 * Non-optimized dictionary for LWZ encode and decode. For better performance,
 * different dictionaries could be used for encode and decode.
 * 
 * @author jonranstrom
 *
 */
public class DictionaryLZW {
	private ArrayList<String> dictStore;
	private int wordSize = 4; // getWordSize() below. Not used yet.
	private int startingCodeWidth = 5; // sufficient for A-Z

	/**
	 * Should use log4j. Staying simple.
	 */
	private static class LOGGER {
		static void error(String input) {
			System.out.println(" ERROR " + input);
		}
	}

	public DictionaryLZW() {
		dictStore = new ArrayList<String>();
		// initialize all strings of length one. As the problem is only capital
		// letters
		// this is A-Z that goes into positions 1 through 26.
		// Position 0 is set to "#" matching the wikipedia LZW article. It will
		// not be used.
		// A at 00001, etc. will match the wikipedia test cases.
		dictStore.add("#");
		for (char c = 'A'; c <= 'Z'; c++) {
			dictStore.add(Character.toString(c));
		}
	}

	/* trivial call through. Subtracts out the unused [0] position */
	public int size() {
		return dictStore.size() - 1;
	}

	/*
	 * trivial call through. Will throw on out of bounds However, on index 0,
	 * return empty string instead of the initialized value. Notice negative
	 * value is allowed to throw. (A unit test checks that.)
	 */
	public String getStringAt(int i) {
		return (i != 0) ? dictStore.get(i) : "";
	}

	/**
	 * returns the index number if found, -1 if not. Does not throw on null
	 * input. Still returns -1
	 * 
	 */
	public int findEntry(String word) {
		if (null == word || word.isEmpty())
			LOGGER.error("null or empty value in findEntry()");
		else {
			for (int i = 0; i < dictStore.size(); i++) {
				if (word.equals(dictStore.get(i))) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * Add this word to the dictionary. It should not already be in the
	 * dictionary. If the request input value is out of range, nothing happens
	 * except for console errors.
	 * 
	 */
	public void addEntry(String word) {
		if (null == word) {
			LOGGER.error("null value in addEntry()");
		} else if (!checkEntry(word)) {
			LOGGER.error("bad input value to addEntry(): " + word);
		} else {
			/*
			 * temporary: check the assumption it is not there remove later and
			 * adjust unit test if this was tested.
			 */
			if (findEntry(word) > -1)
				LOGGER.error("addEntry() called when " + word + " is already in dictionary.");
			else {
				/*
				 * if (word.length() > getWordSize()) LOGGER.error(
				 * "Too long word being added to dictionary. Adding anyway");
				 */
				dictStore.add(word);
			}
		}
	}

	/**
	 * For decoding, we add words just as for coding, but we need to know when
	 * the code width changes.
	 */
	public int addEntryGetCodeWidth(String word, int minimumWidth) {
		addEntry(word);
		// The new word went in as the last entry.

		int newIndex = this.size();
		/*
		 * int testIndex = this.findEntry(word); if (newIndex != testIndex) {
		 * LOGGER.error("bad logic"); // never seen }
		 */

		// The code width in this implementation is length of the binary string.
		// That should be log base 2 of the index, rounded up. Doing it the
		// brain-dead way.
		String newCode = BinaryString.toString(newIndex, minimumWidth);
		return newCode.length();
	}

	/**
	 * Check validity before allowing it into our dictionary
	 * 
	 */
	public boolean checkEntry(String word) {
		if (word == null) {
			return false;
		} else
			return word.matches("[A-Z]+");
	}

	/**
	 * As all indices from 1 to size() are valid, it is easy to know if an
	 * integer represents a word in the dictionary
	 */
	public boolean indexExists(int codeIndex) {
		return codeIndex > 0 && codeIndex <= size();
	}

	public int getWordSize() {
		return wordSize;
	}

	public int getStartingCodeSize() {
		return startingCodeWidth;
	}

}
