package org.beer.app.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ConvertDates {

	private ConvertDates() {
	}

	public static String formatDates(String line, String year) {
		// d.m. --> yyyy-mm-dd
		// d.mm. --> yyyy-mm-dd
		// dd.m. --> yyyy-mm-dd
		// dd.mm. --> yyyy-mm-dd

		// d.m --> yyyy-mm-dd
		// d.mm --> yyyy-mm-dd
		// dd.m --> yyyy-mm-dd
		// dd.mm --> yyyy-mm-dd

		// d.m.yy. --> yyyy-mm-dd
		// d.mm.yy. --> yyyy-mm-dd
		// dd.m.yy. --> yyyy-mm-dd
		// dd.mm.yy. --> yyyy-mm-dd

		// d.m.yy --> yyyy-mm-dd
		// d.mm.yy --> yyyy-mm-dd
		// dd.m.yy --> yyyy-mm-dd
		// dd.mm.yy --> yyyy-mm-dd

		// d.m.yyyy. --> yyyy-mm-dd
		// d.mm.yyyy. --> yyyy-mm-dd
		// dd.m.yyyy. --> yyyy-mm-dd
		// dd.mm.yyyy. --> yyyy-mm-dd

		// d.m.yyyy --> yyyy-mm-dd
		// d.mm.yyyy --> yyyy-mm-dd
		// dd.m.yyyy --> yyyy-mm-dd
		// dd.mm.yyyy --> yyyy-mm-dd

		// m-yyyy --> yyyy-mm
		// mm-yyyy --> yyyy-mm

		// ddmmyyyy --> yyyy-mm-dd

		String formattedLine = line;
		formattedLine = dateFormatter1(formattedLine);
		formattedLine = dateFormatter2(formattedLine);
		formattedLine = dateFormatter3(formattedLine);
		formattedLine = dateFormatter4(formattedLine, year);
		return formattedLine;
	}

	/**
	 * ddmmyyyy --> yyyy-mm-dd
	 * 
	 * @param date
	 * @return date in yyyy-MM-dd format
	 * 
	 */
	protected static String dateFormatter1(String date) {
		Pattern p = Pattern.compile("\\d{8}");
		Matcher m = p.matcher(date);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String text = m.group();
			StringBuffer formattedDate = new StringBuffer();
			formattedDate.append(text.substring(4, 8));
			formattedDate.append("-");
			formattedDate.append(text.substring(2, 4));
			formattedDate.append("-");
			formattedDate.append(text.substring(0, 2));
			m.appendReplacement(sb, formattedDate.toString());
		}
		m.appendTail(sb);
		return sb.toString().replaceFirst("\\s+$", "");
	}

	/**
	 * dd.mm.yyyy --> yyyy-mm-dd d.m.yyyy --> yyyy-mm-dd d.m.yy --> yyyy-mm-dd
	 * 
	 * Two first digits of the year are expected to be '20'. One digit month
	 * and/or day are filled with leading '0'.
	 * 
	 * @param date
	 * @return date in yyyy-MM-dd format
	 * 
	 */
	protected static String dateFormatter2(String date) {
		Pattern p = Pattern.compile("\\d{1,2}\\.\\d{1,2}\\.\\d{2,4}");
		Matcher m = p.matcher(date);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String text = m.group();
			int firstDot = text.indexOf(".");
			int lastDot = text.lastIndexOf(".");

			StringBuffer year = new StringBuffer();
			StringBuffer month = new StringBuffer();
			StringBuffer day = new StringBuffer();
			year.append(text.substring(lastDot + 1, text.length()));
			month.append(text.substring(firstDot + 1, lastDot));
			day.append(text.substring(0, firstDot));

			if (year.length() < 4) {
				year.insert(0, "20");
			}

			if (month.length() < 2) {
				month.insert(0, "0");
			}

			if (day.length() < 2) {
				day.insert(0, "0");
			}
			StringBuffer formattedDate = new StringBuffer();
			formattedDate.append(year);
			formattedDate.append("-");
			formattedDate.append(month);
			formattedDate.append("-");
			formattedDate.append(day);

			m.appendReplacement(sb, formattedDate.toString());
		}
		m.appendTail(sb);
		return sb.toString().replaceFirst("\\s+$", "");
	}

	/**
	 * dd.mm --> yyyy-mm-dd d.m --> yyyy-mm-dd
	 * 
	 * One digit month and/or day are filled with leading '0'.
	 * 
	 * @param date
	 * @param year
	 * @return date in yyyy-MM-dd format
	 * 
	 */
	protected static String dateFormatter4(String date, String year) {
		Pattern p = Pattern.compile("\\d{1,2}\\.\\d{1,2}");
		Matcher m = p.matcher(date);
		StringBuffer sb = new StringBuffer();

		while (m.find()) {
			String text = m.group();
			int dot = text.indexOf(".");

			StringBuffer month = new StringBuffer();
			StringBuffer day = new StringBuffer();
			month.append(text.substring(dot + 1, text.length()));
			day.append(text.substring(0, dot));

			if (month.length() < 2) {
				month.insert(0, "0");
			}

			if (day.length() < 2) {
				day.insert(0, "0");
			}

			StringBuffer formattedDate = new StringBuffer();
			formattedDate.append(year);
			formattedDate.append("-");
			formattedDate.append(month);
			formattedDate.append("-");
			formattedDate.append(day);

			m.appendReplacement(sb, formattedDate.toString());
		}
		m.appendTail(sb);
		return sb.toString().replaceFirst("\\s+$", "");

	}

	/**
	 * mm.yyyy --> yyyy-mm m.yyyy --> yyyy-mm
	 * 
	 * One digit month is filled with leading '0'.
	 * 
	 * @param date
	 * @param year
	 * @return date in yyyy-MM format
	 * 
	 */
	protected static String dateFormatter3(String date) {
		Pattern p = Pattern.compile("\\d{1,2}\\.\\d{4}");
		Matcher m = p.matcher(date);
		StringBuffer sb = new StringBuffer();

		while (m.find()) {
			String text = m.group();
			int dot = text.indexOf(".");
			StringBuffer year = new StringBuffer();
			StringBuffer month = new StringBuffer();
			year.append(text.substring(dot + 1, dot + 5));
			month.append(text.substring(0, dot));

			if (month.length() < 2) {
				month.insert(0, "0");
			}

			StringBuffer formattedDate = new StringBuffer();
			formattedDate.append(year);
			formattedDate.append("-");
			formattedDate.append(month);

			m.appendReplacement(sb, formattedDate.toString());
		}
		m.appendTail(sb);
		return sb.toString().replaceFirst("\\s+$", "");
	}

	public static String addDate(String previousDate, String line) {
		StringBuffer sb = new StringBuffer();
		sb.append(previousDate);
		sb.append(". ");
		sb.append(line);
		return sb.toString();
	}

	public static boolean hasDate(String line) {
		return line.matches("^\\d{4}-\\d{2}-\\d{2}.*");
	}

	public static String getDate(String formattedLine) {
		return formattedLine.substring(0, 10);
	}
}
