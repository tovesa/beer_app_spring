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
		String inputFileSeparator = getSeparator(lines);
		List<String> formattedLines = ConvertFile.formatLines(lines, inputFileSeparator);
		BeerRatingFileWriter.writeFile(outputFile, formattedLines);
	}

	private static String getSeparator(List<String> lines) {
		String separator = ";";
		for (String line : lines) {
			if (line.contains("#SEPARATOR")) {
				separator = line.substring(line.length() - 1, line.length());
				break;
			}
		}
		return separator;
	}

	private static void removeEmptyLines(List<String> lines) {
		lines.removeIf(p -> p.isEmpty());
	}

	private static List<String> formatLines(List<String> lines, String inputFileSeparator) {
		List<String> formattedLines = new ArrayList<>();
		for (String line : lines) {
			String formattedLine = ConvertSpaces.trimSpaces(line);
			if (formattedLine.startsWith("#")) {
				continue;
			}
			if (".".equals(inputFileSeparator)) {
				formattedLine = ConvertPunctuationMarks.addEndingDot(formattedLine);
				formattedLine = ConvertOrder.moveScore(formattedLine);
				formattedLine = ConvertPunctuationMarks.dotsToSemicolons(formattedLine);
				formattedLine = ConvertAbbreviations.splitScore(formattedLine);
			}
			if (BeerRatingValidator.isValid(formattedLine)) {
				formattedLines.add(formattedLine);
			}
		}
		return formattedLines;
	}
}
