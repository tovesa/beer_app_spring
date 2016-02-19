package org.beer.app.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConvertOrder {

	public static boolean formatOrder(String line) {
		String formattedLine = line;
		boolean toRet = moveScore(formattedLine);
		return toRet;
	}

	public static boolean moveScore(String line) {
		return line.matches(".*\\d{6}\\.$");
	}

	public static boolean isBrewInfoMissing(String line) {
		return line.matches(".*bbe\\s{1}\\d{4}-\\d{2}-\\d{2}\\.\\s{1}\\d{6}.*");
	}

	public static int getNumberOfDots(String line) {
		int count = line.length() - line.replace(".", "").length();
		return count;
	}

	public static boolean isPackMissing(String line) {
		return line.matches(".*(Bottle\\s|Can\\s|Draft\\.|Cask\\.).*");
	}

	public static boolean atHome(String line) {
		return line.matches(".*(Pirkkala\\.|Ruovesi\\.).*");
	}

	public static String getPack(String line) {
		String pack = "";
		Pattern pattern = Pattern.compile("(Bottle\\s|Can\\s|Draft\\.|Cask\\.)");
		Matcher matcher = pattern.matcher(line);
		if (matcher.find()) {
			pack = (matcher.group(0));
		}
		return pack;
	}

	public static String addEndingDot(String line) {
		String formattedLine = line + ".";
		formattedLine = formattedLine.replaceAll("\\.\\.", ".");
		return formattedLine;
	}
}
