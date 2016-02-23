package org.beer.app;

public class BeerRatingFileUtil {
	public static String[] tokenizeLine(String line) throws BeerValidationException {
		validateNumberDots(line);
		String[] ratingArray;
		ratingArray = line.split("\\.");
		stripLeadingWhitespace(ratingArray);
		// printToConsole(ratingArray);
		return ratingArray;
	}

	private static void validateNumberDots(String line) throws BeerValidationException {
		int count = line.length() - line.replace(".", "").length();
		if (count != 10) {
			throw new BeerValidationException("Wrong number of dots: " + count);
		}
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
