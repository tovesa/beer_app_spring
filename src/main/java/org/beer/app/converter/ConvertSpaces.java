package org.beer.app.converter;

public final class ConvertSpaces {

	private ConvertSpaces() {
	}

	public static String trimSpaces(String line) {
		String formattedLine = line.trim();
		formattedLine = formattedLine.replaceAll(" +", " ");
		return formattedLine;
	}
}
