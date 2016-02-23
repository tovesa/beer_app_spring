package org.beer.app;

import java.util.ArrayList;
import java.util.List;

import org.beer.app.converter.ConvertFile;
import org.junit.Test;

public class TestBeerAppLocal {

	@Test
	public void testStartBeerApp() {
		BeerApp app = new BeerApp();
		app.start();
	}

	@Test
	public void testConvertFile() {
		String inputFile = "src/main/resources/beers2016_2.txt";
		String outputFile = "src/main/resources/beers_converted_2.txt";
		ConvertFile.convert(inputFile, outputFile);
	}

	@Test
	public void testCreteBeerRatings() {
		String inputFile = "src/main/resources/beers_converted_2.txt";
		List<BeerRating> brList = new ArrayList<>();
		BeerRatingFileReader brReader = new BeerRatingFileReader();
		brList = brReader.readBeerRatingsFromFile(inputFile);
	}

}
