package org.beer.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.beer.app.converter.BeerRatingValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeerRatingFileReader {

	private static final Logger LOG = LoggerFactory.getLogger(BeerRatingFileReader.class);

	private BeerRatingFileReader() {
	}

	public static List<BeerRating> readBeerRatingsFromFile(String file) {
		List<String> lines = readFile(file);
		removeCommentLines(lines);
		return getBeerRatingsAsList(lines);
	}

	public static void removeCommentLines(List<String> lines) {
		lines.removeIf(p -> p.startsWith("#"));
	}

	private static List<BeerRating> getBeerRatingsAsList(List<String> ratings) {
		List<BeerRating> beerRatingList = new ArrayList<>();
		int index = 0;
		for (String rating : ratings) {
			index++;
			try {
				BeerRatingValidator.isValid(rating, index);
				beerRatingList.add(createBeerRating(rating));
			} catch (BeerValidationException e) {
				LOG.error(e.getMessage() + " Line: " + rating);
				LOG.debug("Create beer rating failed: " + e);
			}
		}
		return beerRatingList;
	}

	public static void printBeerRatingsToLog(List<BeerRating> beerRatingList) {
		if (LOG.isDebugEnabled()) {
			for (BeerRating br : beerRatingList) {
				LOG.debug("beerRating :\n" + br.toString());
			}
		}
	}

	private static BeerRating createBeerRating(String line) throws BeerValidationException {
		String[] ratingArray = BeerRatingFileUtil.tokenizeLine(line);
		String ratingDate = ratingArray[0];
		String ratingPlace = ratingArray[1];
		String purchasingDate = ratingArray[2];
		String purchasingPlace = ratingArray[3];
		String name = ratingArray[4];
		String pack = ratingArray[5];
		String bbe = ratingArray[6];
		String brewInfo = ratingArray[7];
		int aroma = Integer.parseInt(ratingArray[8]);
		int appearance = Integer.parseInt(ratingArray[9]);
		int taste = Integer.parseInt(ratingArray[10]);
		int palate = Integer.parseInt(ratingArray[11]);
		int overall = Integer.parseInt(ratingArray[12]);
		String comments = ratingArray.length == 14 ? ratingArray[13] : "";

		return new BeerRating(ratingDate, ratingPlace, purchasingDate, purchasingPlace, name, pack, bbe, brewInfo,
				aroma, appearance, taste, palate, overall, comments, "", "", 0);
	}

	// private static int getAroma(String score) {
	// return Integer.parseInt(score.substring(0, 1));
	// }
	//
	// private static int getAppearance(String score) {
	// return Integer.parseInt(score.substring(1, 2));
	// }
	//
	// private static int getTaste(String score) {
	// return Integer.parseInt(score.substring(2, 3));
	// }
	//
	// private static int getPalate(String score) {
	// return Integer.parseInt(score.substring(3, 4));
	// }
	//
	// private static int getOverall(String score) {
	// return Integer.parseInt(score.substring(4));
	// }

	public static List<String> readFile(String fileName) {
		List<String> lines = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File(fileName)), "UTF8"))) {

			String line = br.readLine();
			while (line != null) {
				lines.add(line);
				line = br.readLine();
			}
		} catch (IOException e) {
			LOG.error("Read file " + fileName + " failed: " + e);
		}
		return lines;
	}
}
