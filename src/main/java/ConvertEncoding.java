
public class ConvertEncoding {
	public static String convertEncoding(String line) {
		String formattedLine;
		StringBuffer sb = new StringBuffer();
		formattedLine = line.replaceAll("Â", "å");
		formattedLine = formattedLine.replaceAll("ˆl", "Øl");
		formattedLine = formattedLine.replaceAll("%%", "ää");

		sb.append(formattedLine);
		return sb.toString();
	}
}
