package org.beer.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RbClient {
	private static final String DUMMY_RB_ID = "0";
	private static final Logger LOG = LoggerFactory.getLogger(RbClient.class);
	private static HttpClient httpClient = HttpClient.getInstance();

	public String enhanceBeerData(String line) {
		String formattedLine = line;
		String beerName;
		String rbId;
		try {
			beerName = getNameFromLine(formattedLine);
			rbId = getRbIdFromLine(formattedLine);
		} catch (BeerValidationException e) {
			LOG.error("Exception: " + e);
			return formattedLine;
		}

		String response = httpClient.get(getUrl(beerName));

		int hits = getNumberOfHits(response);
		LOG.info("Number of hits: " + hits + ", search term: " + beerName);

		if (hits == 1) {
			formattedLine = checkAndUpdateName(response, formattedLine, beerName);
			formattedLine = checkAndUpdateRbId(response, formattedLine, rbId);
		} else if (hits == 0) {
			LOG.error("Beer not found: " + beerName + ". Name and Rb Id not updated");
		} else if (hits > 1) {
			if (isExactMatch(response, beerName)) {
				formattedLine = checkAndUpdateRbIdMultiHit(response, formattedLine, rbId, beerName);
			} else {
				LOG.error(hits + " hits found, but none matches: " + beerName + ". Rb Id not updated");
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

	private static String checkAndUpdateRbId(String response, String line, String rbId) {
		String rbIdInResponse = getRbIdFromResponse(response);
		StringBuilder sb = new StringBuilder();
		sb.append(line);
		if (rbId.equals(DUMMY_RB_ID)) {
			sb.replace(sb.lastIndexOf(DUMMY_RB_ID), sb.lastIndexOf(DUMMY_RB_ID) + 1, rbIdInResponse);
			LOG.info("Rb Id updated: " + rbId + " --> " + rbIdInResponse);
		} else if (!rbId.equals(rbIdInResponse)) {
			LOG.info("Rb Id has been changed: " + rbId + " --> " + rbIdInResponse
					+ " but not updated. This should never happen.");
		}
		return sb.toString();
	}

	private static String checkAndUpdateRbIdMultiHit(String response, String line, String rbId, String beerName) {
		String rbIdInResponse = getRbIdFromMultiHitResponse(response, beerName);
		StringBuilder sb = new StringBuilder();
		sb.append(line);
		if (rbId.equals(DUMMY_RB_ID)) {
			sb.replace(sb.lastIndexOf(DUMMY_RB_ID), sb.lastIndexOf(DUMMY_RB_ID) + 1, rbIdInResponse);
			LOG.info("Rb Id updated: " + rbId + " --> " + rbIdInResponse);
		} else if (!rbId.equals(rbIdInResponse)) {
			LOG.info("Rb Id has been changed: " + rbId + " --> " + rbIdInResponse
					+ " but not updated. This should never happen.");
		}
		return sb.toString();
	}

	private static boolean isExactMatch(String response, String beerName) {
		String searchTerm = ">" + beerName + "</A>";
		if (response.contains(searchTerm)) {
			LOG.info("Exact hit found from multiple hits: " + beerName);
			return true;
		}
		return false;
	}

	private static String getNameFromResponse(String response) {
		int startIndex = response.indexOf("View more info on ") + 18;
		int endIndex = response.indexOf("\">", startIndex);
		return response.substring(startIndex, endIndex);
	}

	private static String getRbIdFromResponse(String response) {
		int startIndex = response.indexOf("/beer/rate/") + 11;
		int endIndex = response.indexOf("/\"", startIndex);
		return response.substring(startIndex, endIndex);
	}

	private static String getRbIdFromMultiHitResponse(String response, String beerName) {
		int beerNameIndex = response.indexOf(beerName);
		int startIndex = response.indexOf("/beer/rate/", beerNameIndex) + 11;
		int endIndex = response.indexOf("/", startIndex);
		return response.substring(startIndex, endIndex);
	}

	private static String getNameFromLine(String line) throws BeerValidationException {
		String[] ratingArray = BeerRatingFileUtil.tokenizeLine(line);
		return ratingArray[4];
	}

	private static String getRbIdFromLine(String line) throws BeerValidationException {
		String[] ratingArray = BeerRatingFileUtil.tokenizeLine(line);
		return ratingArray[14];
	}
}
