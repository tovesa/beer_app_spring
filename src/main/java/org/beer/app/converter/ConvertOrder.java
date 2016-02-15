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

	public static boolean tooManyDots(String line) {
		int count = line.length() - line.replace(".", "").length();
		return (count > 10) ? true : false;
	}
}
