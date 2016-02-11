package org.beer.app.converter;

public class ConvertAbbreviations {

	public static String replaceAbbreviations(String line) {
		String formattedLine;
		StringBuffer sb = new StringBuffer();
		formattedLine = line.replaceAll("pla", "Pirkkala");
		formattedLine = formattedLine.replaceAll("Pla", "Pirkkala");
		formattedLine = formattedLine.replaceAll("Naamat", "Kahdet Kasvot");
		formattedLine = formattedLine.replaceAll(". Tuulensuu", ". Gastropub Tuulensuu");
		formattedLine = formattedLine.replaceAll("Haras", "O'Hara's Freehouse");
		formattedLine = formattedLine.replaceAll(". Nordic", ". Gastropub Nordic");
		formattedLine = formattedLine.replaceAll(". Apina", ". Kultainen Apina");
		formattedLine = formattedLine.replaceAll("bh ", "Beer Hunters ");
		formattedLine = formattedLine.replaceAll("bd ", "BrewDog ");
		formattedLine = formattedLine.replaceAll("BD ", "BrewDog ");
		formattedLine = formattedLine.replaceAll("hana", "Draft");
		formattedLine = formattedLine.replaceAll("koikkari", "Koivistonkylä");
		formattedLine = formattedLine.replaceAll("cm ", "K-citymarket ");
		formattedLine = formattedLine.replaceAll("Cm ", "K-citymarket ");
		formattedLine = formattedLine.replaceAll("CM ", "K-citymarket ");
		formattedLine = formattedLine.replaceAll("SM ", "S-market ");
		formattedLine = formattedLine.replaceAll("Sm ", "S-market ");
		formattedLine = formattedLine.replaceAll("sm ", "S-market ");
		formattedLine = formattedLine.replaceAll("Partola", "Pirkkala");

		sb.append(formattedLine);
		return sb.toString();
	}
}
