package org.beer.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeerRatingFileReader {

	private static final transient Logger LOG = LoggerFactory.getLogger(BeerRatingFileReader.class);

	public List<BeerRating> readBeerRatingsFromFile(String file) {
		List<String> lines = readFile(file);
		removeCommentLines(lines);
		List<BeerRating> beerRatingList = getBeerRatingsAsList(lines);
		return beerRatingList;
	}

	private static void removeCommentLines(List<String> lines) {
		lines.removeIf(p -> p.startsWith("#"));
	}

	private static List<BeerRating> getBeerRatingsAsList(List<String> ratings) {
		List<BeerRating> beerRatingList = new ArrayList<>();
		for (String rating : ratings) {
			try {
				beerRatingList.add(createBeerRating(rating));
			} catch (BeerValidationException e) {
				LOG.error(e.getMessage() + " Line: " + rating);
			}
		}
		// printBeerRatingsToLog(beerRatingList);
		return beerRatingList;
	}

	private static void printBeerRatingsToLog(List<BeerRating> beerRatingList) {
		if (LOG.isDebugEnabled()) {
			for (BeerRating br : beerRatingList) {
				LOG.debug("beerRating :\n" + br.toString());
			}
		}
	}

	private static BeerRating createBeerRating(String line) throws BeerValidationException {
		String ratingArray[] = null;
		ratingArray = BeerRatingFileUtil.tokenizeLine(line);

		String ratingDate = ratingArray[0];
		String ratingPlace = ratingArray[1];
		String purchasingDate = ratingArray[2];
		String purchasingPlace = ratingArray[3];
		String name = ratingArray[4];
		String pack = ratingArray[5];
		String bbe = ratingArray[6];
		String brewInfo = ratingArray[7];
		int aroma = getAroma(ratingArray[8]);
		int appearance = getAppearance(ratingArray[8]);
		int taste = getTaste(ratingArray[8]);
		int palate = getPalate(ratingArray[8]);
		int overall = getOverall(ratingArray[8]);
		String comments = ratingArray[9];

		BeerRating br = new BeerRating(ratingDate, ratingPlace, purchasingDate, purchasingPlace, name, pack, bbe,
				brewInfo, aroma, appearance, taste, palate, overall, comments, "", "", 0);
		return br;
	}

	private static int getAroma(String score) {
		return Integer.parseInt(score.substring(0, 1));
	}

	private static int getAppearance(String score) {
		return Integer.parseInt(score.substring(1, 2));
	}

	private static int getTaste(String score) {
		return Integer.parseInt(score.substring(2, 3));
	}

	private static int getPalate(String score) {
		return Integer.parseInt(score.substring(3, 4));
	}

	private static int getOverall(String score) {
		return Integer.parseInt(score.substring(4));
	}

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
			System.out.println("Exception: " + e);
		}
		return lines;
	}
}
