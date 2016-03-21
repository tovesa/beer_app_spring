package org.beer.app;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
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
	public void testConvertFile2013() {
		String inputFile = "src/main/resources/beers2013.txt";
		String outputFile = "src/main/resources/beers2013_converted.txt";
		ConvertFile.convert(inputFile, outputFile);
	}

	@Test
	public void testConvertFile2016() {
		String inputFile = "src/main/resources/beers2016.txt";
		String outputFile = "src/main/resources/beers2016_converted.txt";
		ConvertFile.convert(inputFile, outputFile);
	}

	@Test
	public void testGenerateBackupFile() {
		testConvertFile2013();
		testConvertFile2016();
		String inputFile2013 = "src/main/resources/beers2013_converted.txt";
		String inputFile2016 = "src/main/resources/beers2016_converted.txt";
		String outputFile = "src/main/resources/beers2008-2016.txt";
		generateBackup(inputFile2013, inputFile2016, outputFile);
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

	private static void generateBackup(String inputFile2013, String inputFile2016, String outputFile) {
		List<String> lines2013 = BeerRatingFileReader.readFile(inputFile2013);
		List<String> lines2016 = BeerRatingFileReader.readFile(inputFile2016);
		List<String> lines = new ArrayList<>();
		lines.addAll(lines2013);
		lines.addAll(lines2016);
		BeerRatingFileWriter.writeFile(outputFile, lines);
	}

}
