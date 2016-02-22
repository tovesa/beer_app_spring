package org.beer.app;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BeerRating {

	private static final transient Logger LOG = LoggerFactory.getLogger(BeerRating.class);

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
		}

		try {
			validateScore();
		} catch (BeerValidationException e) {
			errorMap.putAll(e.getErrorMap());
		}

		try {
			validateComments();
		} catch (BeerValidationException e) {
			errorMap.putAll(e.getErrorMap());
		}

		try {
			validateDateRbId();
		} catch (BeerValidationException e) {
			errorMap.putAll(e.getErrorMap());
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
		String rbUrl = "http://ratebeer.com/Ratings/Beer/Beer-Ratings.asp?BeerID=" + this.rbId;
		HttpClient httpClient = new HttpClient(); // TODO
		httpClient.init(rbUrl);

		int status = 200;

		if (status == 301) {
			LOG.error("Invalid value: rbId:" + this.rbId);
			return false;
		}

		if (status != 200) {
			LOG.warn("rbId validity cannot be verified. HTTP status: " + status);
		}
		return true;
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

	public String getPurchased() {
		return this.purchasingPlace;
	}

	public void setPurchased(String purchased) {
		this.purchasingPlace = purchased;
	}

	public String getPurchasingDate() {
		return this.purchasingDate;
	}

	public void setPurchasingDate(String purchasingDate) {
		this.purchasingDate = purchasingDate;
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

	public String getGrewInfo() {
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

	public String getNotes() {
		return this.comments;
	}

	public void setNotes(String notes) {
		this.comments = notes;
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
		sb.append("ratingDate=");
		sb.append(this.ratingDate);
		sb.append("\nratingPlace=");
		sb.append(this.ratingPlace);
		sb.append("\npurchasingPlace=");
		sb.append(this.purchasingPlace);
		sb.append("\npurchasingPlace=");
		sb.append(this.purchasingDate);
		sb.append("\nname=");
		sb.append(this.name);
		sb.append("\npack=");
		sb.append(this.pack);
		sb.append("\nbbe=");
		sb.append(this.bbe);
		sb.append("\nbrewInfo=");
		sb.append(this.brewInfo);
		sb.append("\naroma=");
		sb.append(this.aroma);
		sb.append("\nappearance=");
		sb.append(this.appearance);
		sb.append("\ntaste=");
		sb.append(this.taste);
		sb.append("\npalate=");
		sb.append(this.palate);
		sb.append("\noverall=");
		sb.append(this.overall);
		sb.append("\ncomments=");
		sb.append(this.comments);
		sb.append("\nebrewery=");
		sb.append(this.brewery);
		sb.append("\ncountry=");
		sb.append(this.country);
		sb.append("\nrbId=");
		sb.append(this.rbId);

		return sb.toString();
	}
}
