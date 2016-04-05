package org.beer.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RbClient {
	private static final Logger LOG = LoggerFactory.getLogger(RbClient.class);
	private static HttpClient httpClient = HttpClient.getInstance();

	public String getRbId(String beerName) {
		String response = httpClient.get(getUrl(beerName));
		return getRbIdFromResponse(response, beerName);
	}

	private static String getUrl(String beerName) {
		return "http://www.ratebeer.com/findbeer.asp?BeerName=" + beerName.replace(" ", "+");
	}

	private static String getRbIdFromResponse(String response, String beerName) {
		String rbId = "";
		int startIndex = response.indexOf("</b> found <b>") + 14;
		int endIndex = response.indexOf("beers</B>") - 1;
		int hits = Integer.parseInt(response.substring(startIndex, endIndex));
		if (hits == 1) {
			isExactMatch(response, beerName);
			rbId = getId(response);
		} else if (hits == 0) {
			LOG.error("Beer not found: " + beerName);
		} else if (hits > 1) {
			if (isExactMatch(response, beerName)) {
				rbId = getIdFromMultipleHits(response, beerName);
			} else {
				LOG.error("More than one beer found: " + hits + ", but none matches: " + beerName);
			}
		}
		return rbId;
	}

	private static boolean isExactMatch(String response, String beerName) {
		return response.contains(beerName);

	}

	private static String getIdFromMultipleHits(String response, String beerName) {
		int startIndex = response.indexOf(""); // TODO
		int endIndex = response.indexOf("", startIndex); // TODO
		String exactMatch = response.substring(startIndex, endIndex);
		int startIndex2 = exactMatch.indexOf("/beer/rate/");
		int endIndex2 = exactMatch.indexOf("/\"", startIndex2);
		return exactMatch.substring(startIndex2, endIndex2);
	}

	private static String getId(String response) {
		int startIndex = response.indexOf("/beer/rate/");
		int endIndex = response.indexOf("/\"", startIndex);
		return response.substring(startIndex, endIndex);
	}
}
