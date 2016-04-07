package org.beer.app;

import org.beer.app.converter.ConvertFile;
import org.junit.Test;

public class TestRbClient {

	@Test
	public void testAddRbIdIfMissing() {
		String inputFile = "src/test/resources/testBeersSemicolonSeparated.txt";
		String outputFile = "src/test/resources/testBeersSemicolonSeparated_converted.txt";
		ConvertFile.enhance(inputFile, outputFile);
	}
}
