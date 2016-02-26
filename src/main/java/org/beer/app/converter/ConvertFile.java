package org.beer.app.converter;

import java.util.ArrayList;
import java.util.List;

import org.beer.app.BeerRatingFileReader;
import org.beer.app.BeerRatingFileWriter;

public class ConvertFile {

	private ConvertFile() {
	}

	public static void convert(String inputFile, String outputFile) {
		List<String> lines = BeerRatingFileReader.readFile(inputFile);
		removeEmptyLines(lines);
		List<String> formattedLines = ConvertFile.formatLines(lines);
		BeerRatingFileWriter.writeFile(outputFile, formattedLines);
	}

	private static void removeEmptyLines(List<String> lines) {
		lines.removeIf(p -> p.isEmpty());
	}

	private static List<String> formatLines(List<String> lines) {
		List<String> formattedLines = new ArrayList<>();
		for (String line : lines) {
			String formattedLine = ConvertSpaces.trimSpaces(line);
			if (formattedLine.startsWith("#")) {
				formattedLines.add(formattedLine);
				continue;
			}
			formattedLine = ConvertPunctuationMarks.addEndingDot(formattedLine);
			formattedLine = ConvertOrder.moveScore(formattedLine);
			formattedLine = ConvertPunctuationMarks.dotsToSemicolons(formattedLine);
			if (BeerRatingValidator.isValid(formattedLine)) {
				formattedLines.add(formattedLine);
			}
		}
		return formattedLines;
	}
}
