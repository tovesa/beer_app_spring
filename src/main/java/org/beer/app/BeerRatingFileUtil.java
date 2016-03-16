package org.beer.app;

public class BeerRatingFileUtil {

	private BeerRatingFileUtil() {
	}

	public static String[] tokenizeLine(String line) throws BeerValidationException {
		String[] ratingArray;
		ratingArray = line.split(";");
		stripLeadingWhitespace(ratingArray);
		// printToConsole(ratingArray);
		return ratingArray;
	}

	private static void stripLeadingWhitespace(String[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = array[i].replaceAll("^\\s", "");
		}
	}

	private static void printToConsole(String array[]) {
		for (int i = 0; i < array.length; i++) {
			System.out.println("array[" + i + "] : " + array[i]);
		}
	}
}
