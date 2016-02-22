package org.beer.app.converter;

import org.beer.app.BeerRatingFileUtil;
import org.beer.app.BeerValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Validator {

	private static final transient Logger LOG = LoggerFactory.getLogger(Validator.class);

	public static void validate(String line) {

		// gormat: *rating date. *rating place. purchasing date.
		// purchasing place. *name. *pack. bbe. brew info. *score. *description.
		// * = mandatory
		String ratingArray[] = null;
		try {
			validateNumberDots(line);
			ratingArray = BeerRatingFileUtil.tokenizeLine(line);
			BeerRatingFileUtil.stripLeadingWhitespace(ratingArray);
			validateRatingDate(ratingArray[0]);
			validateRatingPlace(ratingArray[1]);
			validatePurchasingDate(ratingArray[2]);
			validatePurchasingPlace(ratingArray[3]);
			validateName(ratingArray[4]);
			validatePack(ratingArray[5]);
			validateBbe(ratingArray[6]);
			validateBrewInfo(ratingArray[7]);
			validateScore(ratingArray[8]);
			validateDescription(ratingArray[9]);
		} catch (BeerValidationException e) {
			// printToConsole(ratingArray);
			String fixedLengthErrorMessage = String.format("%1$-50s", e.getMessage());
			LOG.error(fixedLengthErrorMessage + " Line: " + line);
		}
	}

	private static void printToConsole(String array[]) {
		for (int i = 0; i < array.length; i++) {
			System.out.println("array[" + i + "] : " + array[i]);
		}
	}

	public static void validateNumberDots(String line) throws BeerValidationException {
		int count = line.length() - line.replace(".", "").length();
		if (count != 10) {
			throw new BeerValidationException("Wrong number of dots: " + count);
		}

	}

	private static void validateRatingDate(String s) throws BeerValidationException {
		if (!s.matches("^\\d{4}-\\d{2}-\\d{2}.*")) {
			throw new BeerValidationException("Incorrect rating date: " + s);
		}
	}

	private static void validateRatingPlace(String s) throws BeerValidationException {
		if (!s.matches("(.*){4,}")) {
			throw new BeerValidationException("Incorrect rating place: " + s);
		}

	}

	private static void validatePurchasingDate(String s) throws BeerValidationException {
		if (!s.matches("\\d{4}-\\d{2}-\\d{2}") && !s.isEmpty()) {
			throw new BeerValidationException("Incorrect purchasing date: " + s);
		}
	}

	private static void validatePurchasingPlace(String s) throws BeerValidationException {
		if (!s.matches("(.*){4,}") && !s.isEmpty()) {
			throw new BeerValidationException("Incorrect purchasing place: " + s);
		}
	}

	private static void validateName(String s) throws BeerValidationException {
		if (!s.matches("(.*){4,}")) {
			throw new BeerValidationException("Incorrect name: " + s);
		}
	}

	private static void validatePack(String s) throws BeerValidationException {
		if (!s.matches("(Bottle|Can)\\s\\d{3}ml") && !s.matches("(Draft|Cask)")) {
			throw new BeerValidationException("Incorrect pack: " + s);
		}

	}

	private static void validateBbe(String s) throws BeerValidationException {
		if (!s.matches("bbe\\s{1}\\d{4}-\\d{2}-\\d{2}") && !s.isEmpty()) {
			throw new BeerValidationException("Incorrect bbe: " + s);
		}
	}

	private static void validateBrewInfo(String s) throws BeerValidationException {
		if (!s.matches("(.*){4,}") && !s.isEmpty()) {
			throw new BeerValidationException("Incorrect brew info: " + s);
		}

	}

	private static void validateScore(String s) throws BeerValidationException {
		if (!s.matches("\\d{5,6}")) {
			throw new BeerValidationException("Incorrect score: " + s);
		}

	}

	private static void validateDescription(String s) throws BeerValidationException {
		if (!s.matches("(.*){20,}")) {
			throw new BeerValidationException("Incorrect description: " + s);
		}
	}
}
