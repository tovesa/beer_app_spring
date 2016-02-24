package org.beer.app;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.beer.app.converter.ConvertFile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

public class TestBeerAppLocal {

	private static EsClient esClient;

	@Before
	public void setUp() {
		esClient = EsClient.getInstance();
		esClient.start();
	}

	@After
	public void tearDown() {
		esClient.stop();
	}

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
		List<BeerRating> brList = getBeerRatings(inputFile);
		verifyNumberOfCreatedBeerRatings(inputFile, brList);
	}

	@Test
	public void testAddBeerRatingsToEs() throws JsonProcessingException {
		String inputFile = "src/main/resources/beers_converted_2.txt";
		List<BeerRating> brList = getBeerRatings(inputFile);
		for (BeerRating br : brList) {
			esClient.createBeerRating(br);
		}
	}

	@Test
	public void testGetBeerRatingsFromEs() throws BeerValidationException {
		List<BeerRating> brList = esClient.getBeerRatings("name", "Siperia");
		for (BeerRating br : brList) {
			System.out.println("Beer rating from ES:\n" + br.toString());
		}
	}

	private void verifyNumberOfCreatedBeerRatings(String inputFile, List<BeerRating> brList) {
		assertEquals(getNumberOfLines(inputFile), brList.size());

	}

	private int getNumberOfLines(String inputFile) {
		// TODO Auto-generated method stub
		return 0;
	}

	private static List<BeerRating> getBeerRatings(String inputFile) {
		List<BeerRating> brList = new ArrayList<>();
		BeerRatingFileReader brReader = new BeerRatingFileReader();
		brList = brReader.readBeerRatingsFromFile(inputFile);
		return brList;
	}
}
