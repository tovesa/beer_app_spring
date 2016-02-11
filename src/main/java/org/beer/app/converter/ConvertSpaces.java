package org.beer.app.converter;

public class ConvertSpaces {

	public static String trimSpaces(String line) {
		String formattedLine = line.trim();
		formattedLine = formattedLine.replaceAll(" +", " ");
		return formattedLine;
	}

}
