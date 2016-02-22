package org.beer.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BeerRatingFileReader {

	public List<BeerRating> readBeerRatingsFromFile(String file) {

		List<String> lines = readFile(file);
		List<BeerRating> beerRatingList = getBeerRatingsAsList(lines);

		return beerRatingList;
	}

	private static List<BeerRating> getBeerRatingsAsList(List<String> ratings) {
		List<BeerRating> beerRatingList = new ArrayList<>();
		for (String rating : ratings) {
			BeerRating br = createBeerRating(rating);
			beerRatingList.add(br);
		}
		return beerRatingList;
	}

	private static BeerRating createBeerRating(String rating) {
		String ratingDate = getRatingDate(rating);
		String ratingPlace = getRatingPlace(rating);
		String purchasingDate = getPurchasingDate(rating);
		String purchasingPlace = getPurchasingPlace(rating);
		String name = getName(rating);
		String pack = getPack(rating);
		String bbe = getBbe(rating);
		String brewInfo = getBrewInfo(rating);
		int aroma = getAroma(rating);
		int appearance = getAppearance(rating);
		int taste = getTaste(rating);
		int palate = getPalate(rating);
		int overall = getOverall(rating);
		String comments = getComments(rating);
		BeerRating br = new BeerRating(ratingDate, ratingPlace, purchasingDate, purchasingPlace, name, pack, bbe,
				brewInfo, aroma, appearance, taste, palate, overall, comments, "", "", 0);
		return br;
	}

	private static String getRatingDate(String rating) {
		// TODO Auto-generated method stub
		return null;
	}

	private static String getRatingPlace(String rating) {
		// TODO Auto-generated method stub
		return null;
	}

	private static String getPurchasingDate(String rating) {
		// TODO Auto-generated method stub
		return null;
	}

	private static String getPurchasingPlace(String rating) {
		// TODO Auto-generated method stub
		return null;
	}

	private static String getName(String rating) {
		// TODO Auto-generated method stub
		return null;
	}

	private static String getPack(String rating) {
		// TODO Auto-generated method stub
		return null;
	}

	private static String getBbe(String rating) {
		// TODO Auto-generated method stub
		return null;
	}

	private static String getBrewInfo(String rating) {
		// TODO Auto-generated method stub
		return null;
	}

	private static int getAroma(String rating) {
		// TODO Auto-generated method stub
		return 0;
	}

	private static int getAppearance(String rating) {
		// TODO Auto-generated method stub
		return 0;
	}

	private static int getTaste(String rating) {
		// TODO Auto-generated method stub
		return 0;
	}

	private static int getPalate(String rating) {
		// TODO Auto-generated method stub
		return 0;
	}

	private static int getOverall(String rating) {
		// TODO Auto-generated method stub
		return 0;
	}

	private static String getComments(String comments) {
		// TODO Auto-generated method stub
		return null;
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
