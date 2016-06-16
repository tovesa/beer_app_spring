package org.beer.app;

import java.util.List;
import java.util.Map;

import org.beer.app.dao.DataStorage;
import org.beer.app.dao.DataStorageClient;
import org.beer.app.util.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BeerRatingService {

	private static final Logger LOG = LoggerFactory.getLogger(BeerRatingService.class);
	@Autowired
	private Map<String, DataStorageClient> dataStorageClientMap;
	private DataStorageClient dataStorageClient;

	public BeerRatingService() {
		selectStorageClient();
	}

	private void selectStorageClient() {
		String configuredDataStorage = PropertyReader.getProperty("dataStorage", DataStorage.RAMDIRECTORY.getValue());
		this.dataStorageClient = this.dataStorageClientMap.get(configuredDataStorage);
	}

	@RequestMapping(value = "/createRating", method = RequestMethod.POST)
	public ResponseEntity<BeerRating> createRating(@RequestBody BeerRating beerRating) {
		LOG.debug("createRating: " + beerRating.toString());
		try {
			this.dataStorageClient.createBeerRating(beerRating);
		} catch (Exception e) {
			LOG.error("Create rating failed: " + e);
			return new ResponseEntity<>(beerRating, getStatus(e));
		}
		return new ResponseEntity<>(beerRating, HttpStatus.OK);
	}

	@RequestMapping(value = "/getAutoSuggestions")
	public ResponseEntity<String> getAutoSuggestions(@RequestParam("field") String field,
			@RequestParam("term") String term) {
		LOG.debug("field: {}, term: {}", field, term);
		String autoSuggestions = "";
		try {
			autoSuggestions = this.dataStorageClient.getAutoSuggestions(field, term);
		} catch (Exception e) {
			LOG.error("Get auto suggestions failed: " + e);
			return new ResponseEntity<>(autoSuggestions, getStatus(e));
		}
		LOG.debug("Response: {}", autoSuggestions);
		return new ResponseEntity<>(autoSuggestions, HttpStatus.OK);
	}

	@RequestMapping(value = "/getRatings")
	public ResponseEntity<List<BeerRating>> getRatings(@RequestParam("field") String field,
			@RequestParam("term") String term) {
		LOG.debug("field: {}, term: {}", field, term);
		List<BeerRating> beerRatings = null;
		try {
			beerRatings = this.dataStorageClient.getBeerRatings(field, term);
		} catch (Exception e) {
			LOG.error("Get ratings failed: " + e);
			return new ResponseEntity<>(beerRatings, getStatus(e));
		}
		LOG.debug("Response: {}", beerRatings);
		return new ResponseEntity<>(beerRatings, HttpStatus.OK);
	}

	private static HttpStatus getStatus(Exception e) {
		return HttpStatus.BAD_REQUEST; // TODO
	}
}
