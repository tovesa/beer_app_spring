package org.beer.app.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public final class ConvertAbbreviations {

	private ConvertAbbreviations() {
	}

	public static String replaceAbbreviations(String line, String separator) {
		String formattedLine = line;
		StringBuilder sb = new StringBuilder();
		// formattedLine = line.replaceAll("pla", "Pirkkala");
		// formattedLine = formattedLine.replaceAll("Naamat", "Kahdet Kasvot");
		// formattedLine = formattedLine.replaceAll(". Tuulensuu", ". Gastropub
		// Tuulensuu");
		// formattedLine = formattedLine.replaceAll("Haras", "O'Hara's
		// Freehouse");
		// formattedLine = formattedLine.replaceAll("\\. Nordic", ". Gastropub
		// Nordic");
		// formattedLine = formattedLine.replaceAll("\\. Apina", ". Kultainen
		// Apina");
		// formattedLine = formattedLine.replaceAll("bh ", "Beer Hunters ");
		// formattedLine = formattedLine.replaceAll("Bh ", "Beer Hunters ");
		// formattedLine = formattedLine.replaceAll("BH ", "Beer Hunters ");
		// formattedLine = formattedLine.replaceAll("bd ", "BrewDog ");
		// formattedLine = formattedLine.replaceAll("BD ", "BrewDog ");
		// formattedLine = formattedLine.replaceAll("hana", "Draft");
		// formattedLine = formattedLine.replaceAll("koikkari",
		// "Koivistonkylä");
		// formattedLine = formattedLine.replaceAll("cm ", "K-citymarket ");
		// formattedLine = formattedLine.replaceAll("Cm ", "K-citymarket ");
		// formattedLine = formattedLine.replaceAll("CM ", "K-citymarket ");
		// formattedLine = formattedLine.replaceAll("SM ", "S-market ");
		// formattedLine = formattedLine.replaceAll("Sm ", "S-market ");
		// formattedLine = formattedLine.replaceAll("sm ", "S-market ");
		// formattedLine = formattedLine.replaceAll("Partola", "Pirkkala");
		// formattedLine = formattedLine.replaceAll("fullers", "Fuller's");
		// formattedLine = formattedLine.replaceAll("stapa", "Stadin");
		// formattedLine = formattedLine.replaceAll("black door", "Black Door");
		// formattedLine = formattedLine.replaceAll("bier bier", "Bier-Bier");

		if (".".equals(separator)) {
			formattedLine = line.replaceAll(" 33cl", ". Bottle 330ml.");
			formattedLine = formattedLine.replaceAll(" 35cl", ". Bottle 355ml.");
			formattedLine = formattedLine.replaceAll(" 37,5cl", ". Bottle 375ml.");
			formattedLine = formattedLine.replaceAll(" 37.5cl", ". Bottle 375ml.");
			formattedLine = formattedLine.replaceAll(" 650cl", ". Bottle 650ml.");
			formattedLine = formattedLine.replaceAll(" 660cl", ". Bottle 650ml.");
			formattedLine = formattedLine.replaceAll(" 50cl", ". Bottle 500ml.");
			formattedLine = formattedLine.replaceAll(" 75cl", ". Bottle 750ml.");

			formattedLine = formattedLine.replaceAll(" 330\\.", ". Bottle 330ml.");
			formattedLine = formattedLine.replaceAll(" 341\\.", ". Bottle 341ml.");
			formattedLine = formattedLine.replaceAll(" 355\\.", ". Bottle 355ml.");
			formattedLine = formattedLine.replaceAll(" 500\\.", ". Bottle 500ml.");
			formattedLine = formattedLine.replaceAll(" 510\\.", ". Bottle 510ml.");
			formattedLine = formattedLine.replaceAll(" 600\\.", ". Bottle 600ml.");
			formattedLine = formattedLine.replaceAll(" 650\\.", ". Bottle 650ml.");
			formattedLine = formattedLine.replaceAll(" 660\\.", ". Bottle 660ml.");
			formattedLine = formattedLine.replaceAll(" 750\\.", ". Bottle 750ml.");

			formattedLine = formattedLine.replaceAll("Can 330\\.", ". Can 330ml.");
			formattedLine = formattedLine.replaceAll("Can 355\\.", ". Can 355ml.");
			formattedLine = formattedLine.replaceAll("Can 500\\.", ". Can 500ml.");
		} else if (";".equals(separator)) {
			formattedLine = line.replaceAll(";330;", ";Bottle 330ml;");
			formattedLine = formattedLine.replaceAll(";341;", ";Bottle 341ml;");
			formattedLine = formattedLine.replaceAll(";355;", ";Bottle 355ml;");
			formattedLine = formattedLine.replaceAll(";400;", ";Bottle 400ml;");
			formattedLine = formattedLine.replaceAll(";500;", ";Bottle 500ml;");
			formattedLine = formattedLine.replaceAll(";510;", ";Bottle 510ml;");
			formattedLine = formattedLine.replaceAll(";600;", ";Bottle 600ml;");
			formattedLine = formattedLine.replaceAll(";650;", ";Bottle 650ml;");
			formattedLine = formattedLine.replaceAll(";660;", ";Bottle 660ml;");
			formattedLine = formattedLine.replaceAll(";750;", ";Bottle 750ml;");

			formattedLine = formattedLine.replaceAll(";Can 330;", ";Can 330ml;");
			formattedLine = formattedLine.replaceAll(";Can 355;", ";Can 355ml;");
			formattedLine = formattedLine.replaceAll(";Can 500;", ";Can 500ml;");
			formattedLine = formattedLine.replaceAll(";Can 568;", ";Can 568ml;");
		}

		sb.append(formattedLine);
		return sb.toString();
	}

	public static String splitScore(String line) {
		Pattern p = Pattern.compile("\\d{5,6}");
		Matcher m = p.matcher(line);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String text = m.group();
			if (isScore(line, text)) {
				StringBuffer formattedScore = new StringBuffer();
				// it is expected that aroma and taste are only one digit
				// even if official range is 0-10
				formattedScore.append(text.substring(0, 1));
				formattedScore.append(";");
				formattedScore.append(text.substring(1, 2));
				formattedScore.append(";");
				formattedScore.append(text.substring(2, 3));
				formattedScore.append(";");
				formattedScore.append(text.substring(3, 4));
				formattedScore.append(";");
				formattedScore.append(text.substring(4));
				m.appendReplacement(sb, formattedScore.toString());
			}
		}
		m.appendTail(sb);
		return sb.toString().replaceFirst("\\s+$", "");
	}

	private static boolean isScore(String line, String text) {
		String score = line.substring(StringUtils.ordinalIndexOf(line, ";", 8) + 1,
				StringUtils.ordinalIndexOf(line, ";", 9));
		return text.equals(score) ? true : false;
	}
}
