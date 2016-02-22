package org.beer.app.converter;

public class ConvertOrder {

	public static String moveScore(String line) {
		if (isScoreLastWord(line)) {
			int ninthDot = nthIndexOfDot(line, 9);
			int eighthDot = nthIndexOfDot(line, 8);
			StringBuilder sb = new StringBuilder();
			String begin = line.substring(0, eighthDot + 1);
			sb.append(begin);
			String score = line.substring(ninthDot + 1);
			sb.append(score);
			String description = line.substring(eighthDot + 1, ninthDot + 1);
			sb.append(description);
			return sb.toString();
		}
		return line;
	}

	private static boolean isScoreLastWord(String line) {
		return line.matches(".*\\s{1}\\d{5,6}\\.$");
	}

	private static int nthIndexOfDot(String line, int n) {
		final String dot = ".";
		int index = line.indexOf(dot);
		if (index == -1)
			return -1;
		for (int i = 1; i < n; i++) {
			index = line.indexOf(dot, index + 1);
			if (index == -1)
				return -1;
		}
		return index;
	}
}
