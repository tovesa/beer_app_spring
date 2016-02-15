package org.beer.app.converter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ConvertFile {

	public static void main(String args[]) {

		String inputFileName = "src/main/resources/beers2016_2.txt";
		String outputFileName = "src/main/resources/beers_converted_2.txt";

		// String inputFileName = "src/main/resources/beers_converted.txt";
		// String outputFileName = "src/main/resources/beers_converted_2.txt";

		List<String> lines = ConvertFile.readFile(inputFileName);
		// lines.removeIf(p -> p.isEmpty());
		List<String> formattedLines = ConvertFile.formatLines(lines);

		for (int i = 0; i < lines.size(); i++) {
			String oldLine = lines.get(i);
			String newLine = formattedLines.get(i);
			if (!oldLine.equals(newLine)) {
				System.out.println("old: " + oldLine);
				System.out.println("new: " + newLine);
				System.out.println("-----------");
			}
		}

		// ConvertFile.writeFile(outputFileName, formattedLines);
	}

	private static List<String> formatLines(List<String> lines) {

		List<String> formattedLines = new ArrayList<>();
		String year = "2013";
		String date = "2013-08-18";
		for (String line : lines) {
			if (line.startsWith("YEAR:")) {
				year = line.substring(5, 9);
			}
			// String formattedLine = ConvertSpaces.trimSpaces(line);
			// formattedLine = ConvertDates.formatDates(formattedLine, year);
			// if (ConvertDates.hasDate(formattedLine)) {
			// date = ConvertDates.getDate(formattedLine);
			// } else {
			// if (formattedLine.length() > 10) {
			// formattedLine = ConvertDates.addDate(date, formattedLine);
			// }
			// }
			// formattedLine =
			// ConvertAbbreviations.replaceAbbreviations(formattedLine);
			// formattedLine = ConvertCases.convertToUpperCase(formattedLine);
			// formattedLine =
			// ConvertPunctuationMarks.formatPunctuationMarks(formattedLine);
			String formattedLine = line;
			formattedLines.add(formattedLine);
			if (ConvertOrder.formatOrder(formattedLine)) {
				System.out.println("FIX SCORE: " + formattedLine);
			}
			if (ConvertOrder.tooManyDots(formattedLine)) {
				System.out.println("TOO MANY DOTS: " + formattedLine);
			}

		}
		return formattedLines;
	}

	private static List<String> readFile(String fileName) {

		List<String> lines = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File(fileName)), "UTF8"))) {

			String line = br.readLine();

			while (line != null) {
				lines.add(line);
				line = br.readLine();
			}
		} catch (IOException e) {
			System.out.println("Exception: " + e);
		}

		return lines;
	}

	private static void writeFile(String newFileName, List<String> formattedLines) {

		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFileName), "utf-8"))) {
			for (String line : formattedLines) {
				writer.write(line + "\n");
			}
		} catch (IOException e) {
			System.out.println("Exception: " + e);
		}

	}
}
