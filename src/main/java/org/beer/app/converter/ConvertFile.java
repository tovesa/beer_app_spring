package org.beer.app.converter;

import java.util.ArrayList;
import java.util.List;

import org.beer.app.BeerRatingFileWriter;
import org.beer.app.BeerRatingFileReader;

public class ConvertFile {

	public static void main(String args[]) {

		String inputFileName = "src/main/resources/beers2016_2.txt";
		String outputFileName = "src/main/resources/beers_converted_2.txt";

		// String inputFileName = "src/main/resources/beers_converted.txt";
		// String outputFileName = "src/main/resources/beers_converted_2.txt";

		List<String> lines = BeerRatingFileReader.readFile(inputFileName);
		lines.removeIf(p -> p.isEmpty());
		List<String> formattedLines = ConvertFile.formatLines(lines);

		// for (int i = 0; i < lines.size(); i++) {
		// String oldLine = lines.get(i);
		// String newLine = formattedLines.get(i);
		// if (!oldLine.equals(newLine)) {
		// System.out.println("old: " + oldLine);
		// System.out.println("new: " + newLine);
		// System.out.println("-----------");
		// }
		// }

		BeerRatingFileWriter.writeFile(outputFileName, formattedLines);
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
			Validator.validate(formattedLine);
			formattedLines.add(formattedLine);
		}
		return formattedLines;
	}
}
