package org.beer.app.converter;

public class ConvertCases {
	public static String convertToUpperCase(String line) {
		String formattedLine;
		StringBuffer sb = new StringBuffer();
		formattedLine = line.replaceAll("pirkkala", "Pirkkala");
		formattedLine = formattedLine.replaceAll("ruovesi", "Ruovesi");
		formattedLine = formattedLine.replaceAll("konttori", "Konttori");
		formattedLine = formattedLine.replaceAll("tuulensuu", "Tuulensuu");
		formattedLine = formattedLine.replaceAll("kahdet kasvot", "Kahdet Kasvot");
		formattedLine = formattedLine.replaceAll("Kahdet kasvot", "Kahdet Kasvot");
		formattedLine = formattedLine.replaceAll("nordic", "Nordic");
		formattedLine = formattedLine.replaceAll("ukkometso", "Ukkometso");
		formattedLine = formattedLine.replaceAll("haras", "Haras");
		formattedLine = formattedLine.replaceAll("apina", "Apina");
		formattedLine = formattedLine.replaceAll("koivistonkylä", "Koivistonkylä");
		formattedLine = formattedLine.replaceAll("partola", "Partola");
		formattedLine = formattedLine.replaceAll("lielahti", "Lielahti");
		formattedLine = formattedLine.replaceAll("linnainmaa", "Linnainmaa");
		formattedLine = formattedLine.replaceAll("ylöjärvi", "Ylöjärvi");
		formattedLine = formattedLine.replaceAll("kittys", "Kitty's Public House");
		formattedLine = formattedLine.replaceAll("bruuveri", "Bruuveri");
		formattedLine = formattedLine.replaceAll("kittys", "Kitty's Public House");
		formattedLine = formattedLine.replaceAll("plevna", "Plevna");
		formattedLine = formattedLine.replaceAll("stadin", "Stadin");
		formattedLine = formattedLine.replaceAll("mikkeller", "Mikkeller");
		formattedLine = formattedLine.replaceAll("brewdog", "BrewDog");
		formattedLine = formattedLine.replaceAll("Brewdog", "BrewDog");
		formattedLine = formattedLine.replaceAll("de molen", "De Molen");
		formattedLine = formattedLine.replaceAll("De molen", "De Molen");
		formattedLine = formattedLine.replaceAll("to ol", "To Øl");
		formattedLine = formattedLine.replaceAll("to öl", "To Øl");
		formattedLine = formattedLine.replaceAll("To öl", "To Øl");
		formattedLine = formattedLine.replaceAll("To Öl", "To Øl");
		formattedLine = formattedLine.replaceAll("To ˆl", "To Øl");
		formattedLine = formattedLine.replaceAll("N‰rke", "Närke");
		formattedLine = formattedLine.replaceAll("n‰rke", "Närke");
		formattedLine = formattedLine.replaceAll("Â", "å");
		formattedLine = formattedLine.replaceAll("%%", "ää");
		formattedLine = formattedLine.replaceAll("lervig", "Lervig");
		formattedLine = formattedLine.replaceAll("nogne o", "Nøgne Ø");
		formattedLine = formattedLine.replaceAll("nögne ö", "Nøgne Ø");
		formattedLine = formattedLine.replaceAll("Nogne o", "Nøgne Ø");
		formattedLine = formattedLine.replaceAll("Nögne ö", "Nøgne Ø");
		formattedLine = formattedLine.replaceAll("Nogne O", "Nøgne Ø");
		formattedLine = formattedLine.replaceAll("Nögne Ö", "Nøgne Ø");
		formattedLine = formattedLine.replaceAll("nogne", "Nøgne Ø");
		formattedLine = formattedLine.replaceAll("Nogne", "Nøgne Ø");
		formattedLine = formattedLine.replaceAll("Nögne", "Nøgne Ø");
		formattedLine = formattedLine.replaceAll("hiisi", "Hiisi");
		formattedLine = formattedLine.replaceAll("draft", "Draft");
		sb.append(formattedLine);
		return sb.toString();
	}
}
