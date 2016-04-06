package org.beer.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RbClient {
	private static final Logger LOG = LoggerFactory.getLogger(RbClient.class);
	private static HttpClient httpClient = HttpClient.getInstance();

	public String enhanceBeerData(String line) {
		String beerName;
		String formattedLine = line;
		try {
			beerName = getName(formattedLine);
		} catch (BeerValidationException e) {
			LOG.error("BeerValidationException: " + e.getMessage());
			return formattedLine;
		}

		// String response =
		// "aaaaaaaaaaaaaaaaasssssssssssssssssssssssssssssddddddddddd";
		String response = httpClient.get(getUrl(beerName));

		int hits = getNumberOfHits(response);
		LOG.info("Number of hits: " + hits + ", search term: " + beerName);
		if (hits == 1) {
			formattedLine = checkAndUpdateName(response, formattedLine, beerName);
			formattedLine = formattedLine + getId(response);
		} else if (hits == 0) {
			LOG.error("Beer not found: " + beerName);
		} else if (hits > 1) {
			if (isExactMatch(response, beerName)) {
				formattedLine = formattedLine + getIdFromMultipleHits(response, beerName);
			} else {
				LOG.error("More than one beer found: " + hits + ", but none matches: " + beerName);
			}
		}
		return formattedLine;
	}

	private static int getNumberOfHits(String response) {
		int startIndex = response.indexOf("</b> found <b>") + 14;
		int endIndex = response.indexOf("beers</B>") - 1;
		if (startIndex < 14 || endIndex < 14) {
			LOG.error("Unexpected response: " + response);
			return 0;
		}
		return Integer.parseInt(response.substring(startIndex, endIndex));
	}

	private static String getUrl(String beerName) {
		return "http://www.ratebeer.com/findbeer.asp?BeerName=" + beerName.replace(" ", "+");
	}

	private static String checkAndUpdateName(String response, String line, String beerName) {
		String beerNameInResponse = getNameFromResponse(response);
		String formattedLine = line;
		if (!beerName.equals(beerNameInResponse)) {
			formattedLine = line.replace(beerName, beerNameInResponse);
			LOG.info("Beer name updated: " + beerName + " --> " + beerNameInResponse);
		}
		return formattedLine;
	}

	private static String getNameFromResponse(String response) {
		int startIndex = response.indexOf("View more info on ") + 18;
		int endIndex = response.indexOf("\">", startIndex) - 1;
		return response.substring(startIndex, endIndex);
	}

	private static boolean isExactMatch(String response, String beerName) {
		return response.contains(beerName);

	}

	private static String getIdFromMultipleHits(String response, String beerName) {
		int beerNameIndex = response.indexOf(beerName);
		int startIndex = response.indexOf("/beer/rate/", beerNameIndex) + 11;
		int endIndex = response.indexOf("/", startIndex) - 1;
		return response.substring(startIndex, endIndex);
	}

	private static String getId(String response) {
		int startIndex = response.indexOf("/beer/rate/");
		int endIndex = response.indexOf("/\"", startIndex);
		return response.substring(startIndex, endIndex);
	}

	private static String getName(String line) throws BeerValidationException {
		String[] ratingArray = BeerRatingFileUtil.tokenizeLine(line);
		return ratingArray[4];
	}
}
