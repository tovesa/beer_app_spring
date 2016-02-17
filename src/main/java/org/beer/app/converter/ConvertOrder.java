package org.beer.app.converter;

public class ConvertOrder {

	public static boolean formatOrder(String line) {
		String formattedLine = line;
		boolean toRet = moveScore(formattedLine);
		return toRet;
	}

	public static boolean moveScore(String line) {
		return line.matches(".*\\d{6}\\.$");
	}

	public static int isNumberOfDotsCorrect(String line) {
		// *rating date. *rating place. purchasing date. purchasing place.
		// *name. *pack. bbe. brew info. *score. *description.
		int count = line.length() - line.replace(".", "").length();
		if (count == 6 || count == 10 || count == 8) {
			return 0;
		}
		return count;
	}

	public static String addEndingDot(String line) {
		String formattedLine = line + ".";
		formattedLine = formattedLine.replaceAll("\\.\\.", ".");
		return formattedLine;
	}
}
