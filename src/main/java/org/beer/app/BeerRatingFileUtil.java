package org.beer.app;

public class BeerRatingFileUtil {
	public static String[] tokenizeLine(String line) {
		String[] ratingArray;
		ratingArray = line.split("\\.");
		return ratingArray;
	}

	public static void stripLeadingWhitespace(String[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = array[i].replaceAll("^\\s", "");
		}
	}
}
