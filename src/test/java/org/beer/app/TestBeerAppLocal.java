package org.beer.app;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.beer.app.converter.ConvertFile;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

public class TestBeerAppLocal {

	private static EsClient esClient;

	public void startEsClient() {
		esClient = EsClient.getInstance();
		esClient.start();
	}

	public void stopEsClient() {
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
	public void testCreateBeerRatings() {
		String inputFile = "src/main/resources/beers_converted_2.txt";
		List<BeerRating> brList = BeerRatingFileReader.readBeerRatingsFromFile(inputFile);
		verifyNumberOfCreatedBeerRatings(inputFile, brList);
	}

	@Test
	public void testAddBeerRatingsToEs() throws JsonProcessingException {
		startEsClient();
		String inputFile = "src/main/resources/beers_converted_2.txt";
		List<BeerRating> brList = BeerRatingFileReader.readBeerRatingsFromFile(inputFile);
		for (BeerRating br : brList) {
			esClient.createBeerRating(br);
		}
		stopEsClient();
	}

	@Test
	public void testGetBeerRatingsFromEs() throws BeerValidationException {
		startEsClient();
		List<BeerRating> brList = esClient.getBeerRatings("name", "Svaneke Skøre Elg");
		for (BeerRating br : brList) {
			System.out.println("Beer rating from ES:\n" + br.toString());
		}
		stopEsClient();
	}

	private static void verifyNumberOfCreatedBeerRatings(String inputFile, List<BeerRating> brList) {
		assertEquals(getNumberOfLines(inputFile), brList.size());

	}

	private static int getNumberOfLines(String inputFile) {
		List<String> lines = BeerRatingFileReader.readFile(inputFile);
		BeerRatingFileReader.removeCommentLines(lines);
		return lines.size();
	}

}
