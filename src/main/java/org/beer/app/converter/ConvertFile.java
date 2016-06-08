package org.beer.app.converter;

import java.util.ArrayList;
import java.util.List;

import org.beer.app.BeerValidationException;
import org.beer.app.RbClient;
import org.beer.app.util.BeerRatingFileReader;
import org.beer.app.util.BeerRatingFileUtil;
import org.beer.app.util.BeerRatingFileWriter;
import org.beer.app.util.BeerRatingValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConvertFile {

	private static final Logger LOG = LoggerFactory.getLogger(ConvertFile.class);

	private static final RbClient rbClient = new RbClient();

	private ConvertFile() {
	}

	public static void convert(String inputFile, String outputFile) {
		List<String> lines = BeerRatingFileReader.readFile(inputFile);
		removeEmptyLines(lines);
		String inputFileSeparator = getSeparator(lines);
		List<String> formattedLines = ConvertFile.formatLines(lines, inputFileSeparator);
		BeerRatingFileWriter.writeFile(outputFile, formattedLines);
	}

	public static void enhance(String inputFile, String outputFile) {
		List<String> lines = BeerRatingFileReader.readFile(inputFile);
		removeEmptyLines(lines);
		List<String> formattedLines = ConvertFile.enhanceLines(lines);
		BeerRatingFileWriter.writeFile(outputFile, formattedLines);
	}

	public static void validate(String inputFile) {
		List<String> lines = BeerRatingFileReader.readFile(inputFile);
		removeEmptyLines(lines);
		ConvertFile.validateLines(lines);
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
		int lineNumber = 0;
		for (String line : lines) {
			lineNumber++;
			String formattedLine = ConvertSpaces.trimSpaces(line);
			if (formattedLine.startsWith("#") || formattedLine.startsWith("TODO")) {
				continue;
			}
			if (".".equals(inputFileSeparator)) {
				if (!correctNumberOfDots(line, lineNumber)) {
					continue;
				}
				formattedLine = ConvertOrder.moveScore(formattedLine);
				formattedLine = ConvertPunctuationMarks.removeFinalDot(formattedLine);
				formattedLine = ConvertPunctuationMarks.dotsToSemicolons(formattedLine);
				formattedLine = ConvertAbbreviations.splitScore(formattedLine);
			} else if (";".equals(inputFileSeparator)) {
				formattedLine = ConvertAbbreviations.replaceAbbreviations(formattedLine, inputFileSeparator);
			}
			formattedLine = addDummyComments(formattedLine);
			formattedLine = addDummyRbId(formattedLine);

			if (BeerRatingValidator.isValid(formattedLine, lineNumber)) {
				formattedLines.add(formattedLine);
			}
		}

		return formattedLines;
	}

	private static List<String> enhanceLines(List<String> lines) {
		List<String> formattedLines = new ArrayList<>();
		int lineNumber = 0;
		for (String line : lines) {
			lineNumber++;
			String formattedLine = ConvertSpaces.trimSpaces(line);
			if (formattedLine.startsWith("#")) {
				continue;
			}
			formattedLine = rbClient.enhanceBeerData(formattedLine, lineNumber);
			if (BeerRatingValidator.isValid(formattedLine, lineNumber)) {
				formattedLines.add(formattedLine);
			}
		}
		return formattedLines;
	}

	private static void validateLines(List<String> lines) {
		int lineNumber = 0;
		for (String line : lines) {
			lineNumber++;
			if (line.startsWith("#")) {
				continue;
			}
			BeerRatingValidator.isValid(line, lineNumber);
		}
	}

	private static String addDummyComments(String line) {
		if (hasComments(line)) {
			return line;
		}
		String formattedLine = line;
		if (!formattedLine.endsWith(";")) {
			formattedLine = formattedLine + ";";
		}
		formattedLine = formattedLine + "No textual comments";
		return formattedLine;
	}

	private static String addDummyRbId(String line) {
		if (hasRbId(line)) {
			return line;
		}
		String formattedLine = line;
		if (!formattedLine.endsWith(";")) {
			formattedLine = formattedLine + ";";
		}
		formattedLine = formattedLine + "0";
		return formattedLine;
	}

	private static boolean hasComments(String line) {
		String[] ratingArray;
		try {
			ratingArray = BeerRatingFileUtil.tokenizeLine(line);
		} catch (BeerValidationException e) {
			LOG.error("Exception: " + e);
			return false;
		}
		return ratingArray.length < 14 || ratingArray[13].isEmpty() ? false : true;
	}

	private static boolean hasRbId(String line) {
		String[] ratingArray;
		try {
			ratingArray = BeerRatingFileUtil.tokenizeLine(line);
		} catch (BeerValidationException e) {
			LOG.error("Exception: " + e);
			return false;
		}
		return ratingArray.length < 15 || ratingArray[14].isEmpty() ? false : true;
	}

	private static boolean correctNumberOfDots(String line, int lineNumber) {
		int expectedNumberOfDots = line.charAt(line.length() - 1) == '.' ? 10 : 9;
		int count = line.length() - line.replace(".", "").length();
		if (count != expectedNumberOfDots) {
			LOG.error("Incorrect number of dots: " + count + ". Line " + lineNumber + ": " + line);
			return false;
		}
		return true;
	}

	public static int split(String inputFile, String outputFilePrefix) {

		List<String> lines = BeerRatingFileReader.readFile(inputFile);
		removeEmptyLines(lines);

		final int maxNumberOfLines = 199;
		final int indexOfLastLine = lines.size();
		int fromIndex = 0;
		int outputFileNumber = 0;
		boolean linesToHandle = true;
		while (linesToHandle) {
			outputFileNumber++;
			int toIndex = getToIndex(fromIndex, indexOfLastLine, maxNumberOfLines);
			System.out.println("from: " + fromIndex + " to: " + toIndex + " file number: " + outputFileNumber);
			BeerRatingFileWriter.writeFile(getFileName(outputFilePrefix, outputFileNumber),
					lines.subList(fromIndex, toIndex));
			fromIndex = toIndex + 1;
			linesToHandle = toIndex < indexOfLastLine ? true : false;
		}
		return outputFileNumber;
	}

	public static void merge(String inputFilePrefix, String outputFile, int numberOfFiles) {
		List<String> lines = new ArrayList<>();
		for (int i = 1; i <= numberOfFiles; i++) {
			List<String> subLines = BeerRatingFileReader.readFile(getFileName(inputFilePrefix, i));
			lines.addAll(subLines);
		}
		BeerRatingFileWriter.writeFile(outputFile, lines);

	}

	private static int getToIndex(int fromIndex, int indexOfLastLine, int maxNumberOfLines) {
		return fromIndex + maxNumberOfLines < indexOfLastLine ? fromIndex + maxNumberOfLines : indexOfLastLine;
	}

	private static String getFileName(String fileNamePrefix, int fileNumber) {
		return fileNamePrefix + fileNumber + ".txt";
	}
}
