package org.beer.app.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.beer.app.BeerRatingFileUtil;
import org.beer.app.BeerValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BeerRatingValidator {

	private static final Logger LOG = LoggerFactory.getLogger(BeerRatingValidator.class);

	private BeerRatingValidator() {
	}

	public static boolean isValid(String line, int lineNumber) {
		// format: rating date;rating place;purchasing date;purchasing
		// place;name;pack;bbe;brew info;score;description
		try {
			validateNumberOfSemicolons(line);
			String[] ratingArray = BeerRatingFileUtil.tokenizeLine(line);
			validateRatingDate(ratingArray[0]);
			validateRatingPlace(ratingArray[1]);
			validatePurchasingDate(ratingArray[2]);
			validatePurchasingPlace(ratingArray[3]);
			validateName(ratingArray[4]);
			validatePack(ratingArray[5]);
			validateBbe(ratingArray[6]);
			validateBrewInfo(ratingArray[7]);
			validateAroma(ratingArray[8]);
			validateAppearance(ratingArray[9]);
			validateTaste(ratingArray[10]);
			validatePalate(ratingArray[11]);
			validateOverall(ratingArray[12]);
			if (hasDescription(ratingArray)) {
				validateDescription(ratingArray[13]);
			}
		} catch (BeerValidationException e) {
			String fixedLengthErrorMessage = String.format("%1$-50s", e.getMessage());
			String fixedLengthLineNumber = String.format("%04d", lineNumber);
			LOG.error(fixedLengthErrorMessage + " Line " + fixedLengthLineNumber + " : " + line);
			// return false; TODO remove comments
		} catch (ArrayIndexOutOfBoundsException e) {
			String fixedLengthErrorMessage = String.format("%1$-50s",
					"ArrayIndexOutOfBoundsException: " + e.getMessage());
			String fixedLengthLineNumber = String.format("%04d", lineNumber);
			LOG.error(fixedLengthErrorMessage + " Line " + fixedLengthLineNumber + " : " + line);
			return false;
		}
		return true;
	}

	private static boolean hasDescription(String[] ratingArray) {
		return ratingArray.length == 14 ? true : false;
	}

	private static void validateNumberOfSemicolons(String line) throws BeerValidationException {
		int count = line.length() - line.replace(";", "").length();
		if (count != 13) {
			throw new BeerValidationException("Wrong number of semicolons: " + count);
		}
	}

	private static void validateRatingDate(String s) throws BeerValidationException {
		if (!isValidTimeStamp(s)) {
			throw new BeerValidationException("Incorrect rating date: " + s);
		}
	}

	private static void validateRatingPlace(String s) throws BeerValidationException {
		if (!s.matches("(.*){4,}")) {
			throw new BeerValidationException("Incorrect rating place: " + s);
		}

	}

	private static void validatePurchasingDate(String s) throws BeerValidationException {
		if (!isValidTimeStamp(s) && !s.isEmpty()) {
			throw new BeerValidationException("Incorrect purchasing date: " + s);
		}
	}

	private static void validatePurchasingPlace(String s) throws BeerValidationException {
		if (!s.matches("(.*){6,}") && !s.isEmpty()) {
			throw new BeerValidationException("Incorrect purchasing place: " + s);
		}
	}

	private static void validateName(String s) throws BeerValidationException {
		if (s.length() < 3) {
			throw new BeerValidationException("Incorrect name: " + s);
		}
	}

	private static void validatePack(String s) throws BeerValidationException {
		if (!s.matches("(Bottle|Can)\\s\\d{2,3}ml") && !s.matches("(Draft|Cask|Sample)")) {
			throw new BeerValidationException("Incorrect pack: " + s);
		}

	}

	private static void validateBbe(String s) throws BeerValidationException {
		if (!s.matches("\\d{4}-\\d{2}-\\d{2}") && !s.isEmpty()) {
			throw new BeerValidationException("Incorrect bbe: " + s);
		}
	}

	private static void validateBrewInfo(String s) throws BeerValidationException {
		if (!s.matches("(.*){4,}") && !s.isEmpty()) {
			throw new BeerValidationException("Incorrect brew info: " + s);
		}
	}

	private static void validateAroma(String s) throws BeerValidationException {
		if (!s.matches("([1-9]|10)")) {
			throw new BeerValidationException("Incorrect aroma: " + s);
		}
	}

	private static void validateAppearance(String s) throws BeerValidationException {
		if (!s.matches("[1-5]")) {
			throw new BeerValidationException("Incorrect appearance: " + s);
		}
	}

	private static void validateTaste(String s) throws BeerValidationException {
		if (!s.matches("([1-9]|10)")) {
			throw new BeerValidationException("Incorrect taste: " + s);
		}
	}

	private static void validatePalate(String s) throws BeerValidationException {
		if (!s.matches("[1-5]")) {
			throw new BeerValidationException("Incorrect palate: " + s);
		}
	}

	private static void validateOverall(String s) throws BeerValidationException {
		if (!s.matches("([1-9]|1[0-9]|20)")) {
			throw new BeerValidationException("Incorrect overall: " + s);
		}
	}

	private static void validateDescription(String s) throws BeerValidationException {
		if (s.isEmpty() || s.length() < 15) {
			throw new BeerValidationException("Incorrect description: " + s);
		}
		if (s.startsWith("bottle") || s.startsWith("Bottle")) {
			throw new BeerValidationException("Remove pack: " + s);
		}
	}

	private static boolean isValidTimeStamp(String s) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);
		try {
			sdf.parse(s);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

}
