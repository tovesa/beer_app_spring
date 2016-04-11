package org.beer.app;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringEscapeUtils;
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
		String url = RbClient.getUrl(name);
		System.out.println("URL: " + url);
	}

	@Test
	public void testHtmlEscape() {
		String s = "View more info on Põhjala Öö &#40;2014-&#41;";
		System.out.println(StringEscapeUtils.unescapeHtml4(s));
		System.out.println(StringEscapeUtils.escapeHtml4(s));
	}

}
