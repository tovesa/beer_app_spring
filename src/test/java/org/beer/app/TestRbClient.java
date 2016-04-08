package org.beer.app;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import org.beer.app.converter.ConvertFile;
import org.junit.Test;

public class TestRbClient {

	@Test
	public void testAddRbIdIfMissing() {
		String inputFile = "src/test/resources/testBeersSemicolonSeparated.txt";
		String outputFile = "src/test/resources/testBeersSemicolonSeparated_converted.txt";
		ConvertFile.enhance(inputFile, outputFile);
	}

	@Test
	public void testProblematicNames() {
		String inputFile = "src/test/resources/testBeersProblematicNames.txt";
		String outputFile = "src/test/resources/testBeersProblematicNames_converted.txt";
		ConvertFile.enhance(inputFile, outputFile);
	}

	@Test
	public void testGetUrl() throws UnsupportedEncodingException {
		String name = "Põhjala Öö (2014-)";
		String expectedUrl = "http://www.ratebeer.com/findbeer.asp?BeerName=P%F5hjala+%D6%F6+%282014-%29";
		String url = RbClient.getUrl(name);
		System.out.println("URL: " + url);
		assertEquals(expectedUrl, url);

	}

}
