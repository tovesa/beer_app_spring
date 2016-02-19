package org.beer.app.converter;

public class ConvertAbbreviations {

	public static String replaceAbbreviations(String line) {
		String formattedLine;
		StringBuffer sb = new StringBuffer();
		formattedLine = line.replaceAll("pla", "Pirkkala");
		formattedLine = formattedLine.replaceAll("Naamat", "Kahdet Kasvot");
		formattedLine = formattedLine.replaceAll(". Tuulensuu", ". Gastropub Tuulensuu");
		formattedLine = formattedLine.replaceAll("Haras", "O'Hara's Freehouse");
		formattedLine = formattedLine.replaceAll("\\. Nordic", ". Gastropub Nordic");
		formattedLine = formattedLine.replaceAll("\\. Apina", ". Kultainen Apina");
		formattedLine = formattedLine.replaceAll("bh ", "Beer Hunters ");
		formattedLine = formattedLine.replaceAll("Bh ", "Beer Hunters ");
		formattedLine = formattedLine.replaceAll("BH ", "Beer Hunters ");
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
		formattedLine = formattedLine.replaceAll("fullers", "Fuller's");
		formattedLine = formattedLine.replaceAll("stapa", "Stadin");
		formattedLine = formattedLine.replaceAll("black door", "Black Door");
		formattedLine = formattedLine.replaceAll("bier bier", "Bier-Bier");

		formattedLine = formattedLine.replaceAll(" 33cl", ". Bottle 330ml.");
		formattedLine = formattedLine.replaceAll(" 35cl", ". Bottle 355ml.");
		formattedLine = formattedLine.replaceAll(" 37,5cl", ". Bottle 375ml.");
		formattedLine = formattedLine.replaceAll(" 37.5cl", ". Bottle 375ml.");
		formattedLine = formattedLine.replaceAll(" 650cl", ". Bottle 650ml.");
		formattedLine = formattedLine.replaceAll(" 660cl", ". Bottle 650ml.");
		formattedLine = formattedLine.replaceAll(" 50cl", ". Bottle 500ml.");
		formattedLine = formattedLine.replaceAll(" 75cl", ". Bottle 750ml.");

		formattedLine = formattedLine.replaceAll(" 330\\.", ". Bottle 330ml.");
		formattedLine = formattedLine.replaceAll(" 355\\.", ". Bottle 355ml.");
		formattedLine = formattedLine.replaceAll(" 500\\.", ". Bottle 500ml.");
		formattedLine = formattedLine.replaceAll(" 650\\.", ". Bottle 650ml.");
		formattedLine = formattedLine.replaceAll(" 650\\.", ". Bottle 650ml.");
		formattedLine = formattedLine.replaceAll(" 750\\.", ". Bottle 750ml.");

		sb.append(formattedLine);
		return sb.toString();
	}
}
