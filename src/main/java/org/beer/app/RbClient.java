package org.beer.app;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RbClient {
	private static final String DUMMY_RB_ID = "0";
	private static final Logger LOG = LoggerFactory.getLogger(RbClient.class);
	private static HttpClient httpClient = HttpClient.getInstance();

	public String enhanceBeerData(String line, int lineNumber) {
		String formattedLine = line;
		String beerName;
		String rbId;
		try {
			beerName = getNameFromLine(formattedLine);
			rbId = getRbIdFromLine(formattedLine);
			if (!DUMMY_RB_ID.equals(rbId)) {
				return formattedLine;
			}
		} catch (BeerValidationException e) {
			LOG.error("Line " + lineNumber + ": " + e);
			return formattedLine;
		}

		String response = null;
		try {
			response = httpClient.post(getUrl(beerName));
		} catch (UnsupportedEncodingException e) {
			LOG.error("Encode URL failed: " + e);
			return line;
		}

		int hits = getNumberOfHits(response);
		LOG.debug("Number of hits: " + hits + ", search term: " + beerName);

		if (hits == 1 && isAlias(response)) {
			LOG.error("Line number " + lineNumber + ": alias beer: " + beerName + ". Fix name!");
			return line;
		} else if (hits > 2 && isAlias(response)) {
			LOG.warn("Line number " + lineNumber + ": potential alias beer: " + beerName + ". Check this beer.");
		}

		if (hits == 1) {
			formattedLine = checkAndUpdateName(response, formattedLine, beerName);
			formattedLine = checkAndUpdateRbId(response, formattedLine, rbId);
		} else if (hits == 0) {
			LOG.error("Line number " + lineNumber + ": beer not found: " + beerName);
		} else if (hits > 1) {
			if (isExactMatch(response, beerName)) {
				formattedLine = checkAndUpdateRbIdMultiHit(response, formattedLine, rbId, beerName);
			} else {
				LOG.error("Line number " + lineNumber + ": " + hits + " hits found, but no exact match: " + beerName);
			}
		}

		return formattedLine;
	}

	private static boolean isAlias(String response) {
		return response.contains("<span class=rip> A </span>");
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

	protected static String getUrl(String beerName) throws UnsupportedEncodingException {
		final String encoding = "Windows-1252";
		String strippedBeerName = stripProblematicCharacters(beerName);
		return "http://www.ratebeer.com/findbeer.asp?BeerName=" + URLEncoder.encode(strippedBeerName, encoding);
	}

	protected static String stripProblematicCharacters(String beerName) {
		return beerName.replaceAll("['´`’]", "");
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
		String beerName;
		try {
			beerName = getNameFromLine(line);
		} catch (BeerValidationException e) {
			LOG.error("Exception when reading beer name from line: " + e);
			return line;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(line);
		sb.replace(sb.lastIndexOf(DUMMY_RB_ID), sb.lastIndexOf(DUMMY_RB_ID) + 1, rbIdInResponse);
		LOG.info("RbId updated: " + rbId + " --> " + rbIdInResponse + ". Beer: " + beerName);
		return sb.toString();
	}

	private static String checkAndUpdateRbIdMultiHit(String response, String line, String rbId, String beerName) {
		String rbIdInResponse = getRbIdFromMultiHitResponse(response, beerName);
		StringBuilder sb = new StringBuilder();
		sb.append(line);
		sb.replace(sb.lastIndexOf(DUMMY_RB_ID), sb.lastIndexOf(DUMMY_RB_ID) + 1, rbIdInResponse);
		LOG.info("RbId updated: " + rbId + " --> " + rbIdInResponse + ". Beer: " + beerName);
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
