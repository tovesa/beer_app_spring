package org.beer.app;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BeerRating {

	private static final Logger LOG = LoggerFactory.getLogger(BeerRating.class);

	public static final String FN_RATING_DATE = "ratingDate";
	public static final String FN_RATING_PLACE = "ratingPlace";
	public static final String FN_PURCHASING_DATE = "purchasingDate";
	public static final String FN_PURCHASING_PLACE = "purchasingPlace";
	public static final String FN_NAME = "name";
	public static final String FN_PACK = "pack";
	public static final String FN_BBE = "bbe";
	public static final String FN_BREW_INFO = "brewInfo";
	public static final String FN_AROMA = "aroma";
	public static final String FN_APPEARANCE = "appearance";
	public static final String FN_TASTE = "taste";
	public static final String FN_PALATE = "palate";
	public static final String FN_OVERALL = "overall";
	public static final String FN_COMMENTS = "comments";
	public static final String FN_BREWERY = "brewery";
	public static final String FN_COUNTRY = "country";
	public static final String FN_RB_ID = "rbId";

	private static final int COMMENTS_MIN_LENGTH = 30;
	private String ratingDate;
	private String ratingPlace;
	private String purchasingPlace;
	private String purchasingDate;
	private String name;
	private String pack;
	private String bbe;
	private String brewInfo;
	private int aroma;
	private int appearance;
	private int taste;
	private int palate;
	private int overall;
	private String comments;
	private String brewery;
	private String country;
	private int rbId;

	public BeerRating(String ratingDate, String ratingPlace, String purchasingDate, String purchasingPlace, String name,
			String pack, String bbe, String brewInfo, int aroma, int appearance, int taste, int palate, int overall,
			String comments, String brewery, String country, int rbId) {
		this.ratingDate = ratingDate;
		this.ratingPlace = ratingPlace;
		this.purchasingDate = purchasingDate;
		this.purchasingPlace = purchasingPlace;
		this.name = name;
		this.bbe = bbe;
		this.brewInfo = brewInfo;
		this.pack = pack;
		this.aroma = aroma;
		this.appearance = appearance;
		this.taste = taste;
		this.palate = palate;
		this.overall = overall;
		this.comments = comments;
		this.brewery = brewery;
		this.country = country;
		this.rbId = rbId;
	}

	public void validate() throws BeerValidationException {
		Map<String, String> errorMap = new HashMap<>();
		try {
			validateDates();
		} catch (BeerValidationException e) {
			errorMap.putAll(e.getErrorMap());
			LOG.info("Invalid dates detected: " + e);
		}

		try {
			validateScore();
		} catch (BeerValidationException e) {
			errorMap.putAll(e.getErrorMap());
			LOG.info("Invalid score detected: " + e);
		}

		try {
			validateComments();
		} catch (BeerValidationException e) {
			errorMap.putAll(e.getErrorMap());
			LOG.info("Invalid comments detected: " + e);
		}

		try {
			validateDateRbId();
		} catch (BeerValidationException e) {
			errorMap.putAll(e.getErrorMap());
			LOG.info("Invalid RB id detected: " + e);
		}

		if (!errorMap.isEmpty()) {
			logValidationErrors(errorMap);
			throw new BeerValidationException("Validate beer failed.", errorMap);
		}

	}

	private void validateDates() throws BeerValidationException {
		Map<String, String> errorMap = new HashMap<>();

		try {
			validateDate(this.ratingDate, "ratingDate");
		} catch (BeerValidationException e) {
			errorMap.putAll(e.getErrorMap());
		}

		if (!this.purchasingDate.isEmpty()) {
			try {
				validateDate(this.purchasingDate, "purchasingDate");
			} catch (BeerValidationException e) {
				errorMap.putAll(e.getErrorMap());
			}
		}

		if (!this.bbe.isEmpty()) {
			try {
				validateDate(this.bbe, "bbe");
			} catch (BeerValidationException e) {
				errorMap.putAll(e.getErrorMap());
			}
		}

		if (!this.brewInfo.isEmpty()) {
			try {
				validateDate(this.brewInfo, "bottled");
			} catch (BeerValidationException e) {
				errorMap.putAll(e.getErrorMap());
			}
		}

		if (!errorMap.isEmpty()) {
			throw new BeerValidationException("Date validation failed.", errorMap);
		}
	}

	private void validateScore() throws BeerValidationException {
		Map<String, String> errorMap = new HashMap<>();

		if (this.aroma < 0 || this.aroma > 10) {
			errorMap.put("aroma", Integer.toString(this.aroma));
		}

		if (this.appearance < 0 || this.appearance > 5) {
			errorMap.put("appearance", Integer.toString(this.appearance));
		}

		if (this.taste < 0 || this.taste > 10) {
			errorMap.put("taste", Integer.toString(this.taste));
		}

		if (this.palate < 0 || this.palate > 5) {
			errorMap.put("palate", Integer.toString(this.palate));
		}

		if (this.overall < 0 || this.overall > 20) {
			errorMap.put("overall", Integer.toString(this.overall));
		}

		if (!errorMap.isEmpty()) {
			throw new BeerValidationException("Score validation failed.", errorMap);
		}

	}

	private void validateComments() throws BeerValidationException {
		if (this.comments.length() < COMMENTS_MIN_LENGTH) {
			Map<String, String> errorMap = new HashMap<>();
			errorMap.put("comments", this.comments);
			LOG.error("Invalid value: comments too short: " + this.comments.length());
			throw new BeerValidationException("comments validation failed.", errorMap);
		}
	}

	private void validateDateRbId() throws BeerValidationException {
		if (!beerFoundfromRb()) {
			Map<String, String> errorMap = new HashMap<>();
			errorMap.put("rbId", Integer.toString(this.rbId));
			throw new BeerValidationException("rbId validation failed.", errorMap);
		}
	}

	private boolean beerFoundfromRb() {
		return false; // TODO
	}

	private static void validateDate(String date, String dateFieldname) throws BeerValidationException {
		String regex = "((20|21)\\d\\d)-(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])";
		boolean valid = Pattern.matches(regex, date);
		if (!valid) {
			Map<String, String> errorMap = new HashMap<>();
			errorMap.put("dateFieldname", date);
			LOG.error("invalid value: " + dateFieldname + ":" + date);
			throw new BeerValidationException(dateFieldname + " validation failed.", errorMap);
		}
	}

	private static void logValidationErrors(Map<String, String> errorMap) {
		for (Map.Entry<String, String> entry : errorMap.entrySet()) {
			LOG.error("Invalid value: " + entry.getKey() + ":" + entry.getValue());
		}
	}

	private static String createJsonErrorMessage(Map<String, String> errorList) {
		String jsonErrorMesage = null;
		try {
			jsonErrorMesage = new ObjectMapper().writeValueAsString(errorList);
		} catch (JsonProcessingException e) {
			LOG.error("Parse error map to JSON format failed. Exception: " + e.getStackTrace());
		}

		return jsonErrorMesage;
	}

	public String getRatingDate() {
		return this.ratingDate;
	}

	public void setRatingDate(String ratingDate) {
		this.ratingDate = ratingDate;
	}

	public String getRatingPlace() {
		return this.ratingPlace;
	}

	public void setRatingPlace(String ratingPlace) {
		this.ratingPlace = ratingPlace;
	}

	public String getPurchasingDate() {
		return this.purchasingDate;
	}

	public void setPurchasingDate(String purchasingDate) {
		this.purchasingDate = purchasingDate;
	}

	public String getPurchasingPlace() {
		return this.purchasingPlace;
	}

	public void setPurchasingPlace(String purchasingPlace) {
		this.purchasingPlace = purchasingPlace;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrewery() {
		return this.brewery;
	}

	public void setBrewery(String brewery) {
		this.brewery = brewery;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getBbe() {
		return this.bbe;
	}

	public void setBbe(String bbe) {
		this.bbe = bbe;
	}

	public String getBrewInfo() {
		return this.brewInfo;
	}

	public void setBrewInfo(String brewInfo) {
		this.brewInfo = brewInfo;
	}

	public String getPack() {
		return this.pack;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}

	public int getAroma() {
		return this.aroma;
	}

	public void setAroma(int aroma) {
		this.aroma = aroma;
	}

	public int getAppearance() {
		return this.appearance;
	}

	public void setAppearance(int appearance) {
		this.appearance = appearance;
	}

	public int getTaste() {
		return this.taste;
	}

	public void setTaste(int taste) {
		this.taste = taste;
	}

	public int getPalate() {
		return this.palate;
	}

	public void setPalate(int palate) {
		this.palate = palate;
	}

	public int getOverall() {
		return this.overall;
	}

	public void setOverall(int overall) {
		this.overall = overall;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public int getRbId() {
		return this.rbId;
	}

	public void setRbId(int rbId) {
		this.rbId = rbId;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(FN_RATING_DATE);
		sb.append("=");
		sb.append(this.ratingDate);
		sb.append(toStrFormat(FN_RATING_PLACE));
		sb.append(this.ratingPlace);
		sb.append(toStrFormat(FN_PURCHASING_DATE));
		sb.append(this.purchasingDate);
		sb.append(toStrFormat(FN_PURCHASING_PLACE));
		sb.append(this.purchasingPlace);
		sb.append(toStrFormat(FN_NAME));
		sb.append(this.name);
		sb.append(toStrFormat(FN_PACK));
		sb.append(this.pack);
		sb.append(toStrFormat(FN_BBE));
		sb.append(this.bbe);
		sb.append(toStrFormat(FN_BREW_INFO));
		sb.append(this.brewInfo);
		sb.append(toStrFormat(FN_AROMA));
		sb.append(this.aroma);
		sb.append(toStrFormat(FN_APPEARANCE));
		sb.append(this.appearance);
		sb.append(toStrFormat(FN_TASTE));
		sb.append(this.taste);
		sb.append(toStrFormat(FN_PALATE));
		sb.append(this.palate);
		sb.append(toStrFormat(FN_OVERALL));
		sb.append(this.overall);
		sb.append(toStrFormat(FN_COMMENTS));
		sb.append(this.comments);
		sb.append(toStrFormat(FN_BREWERY));
		sb.append(this.brewery);
		sb.append(toStrFormat(FN_COUNTRY));
		sb.append(this.country);
		sb.append(toStrFormat(FN_RB_ID));
		sb.append(this.rbId);

		return sb.toString();
	}

	private static String toStrFormat(String str) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append(str);
		sb.append("=");
		return sb.toString();
	}
}
