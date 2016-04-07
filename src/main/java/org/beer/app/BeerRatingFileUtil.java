package org.beer.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeerRatingFileUtil {

	private static final Logger LOG = LoggerFactory.getLogger(BeerRatingFileUtil.class);

	private BeerRatingFileUtil() {
	}

	public static String[] tokenizeLine(String line) throws BeerValidationException {
		String[] ratingArray;
		ratingArray = line.split(";");
		stripLeadingWhitespace(ratingArray);
		// printToConsole(ratingArray);
		if (ratingArray.length != 15) {
			throw new BeerValidationException("Invalid line: " + line);
		}
		return ratingArray;
	}

	private static void stripLeadingWhitespace(String[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = array[i].replaceAll("^\\s", "");
		}
	}

	private static void printToConsole(String array[]) {
		for (int i = 0; i < array.length; i++) {
			LOG.info("array[" + i + "] : " + array[i]);
		}
	}
}
