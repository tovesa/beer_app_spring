package org.beer.app;

import java.util.List;

import org.beer.app.dao.DataStorage;
import org.beer.app.dao.DataStorageClient;
import org.beer.app.dao.ElasticsearchClient;
import org.beer.app.dao.RamDirectoryClient;
import org.beer.app.util.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private DataStorageClient dataStorageClient;

	public BeerRatingService() {
		this.dataStorageClient = getDataStorageClient();
		this.dataStorageClient.start();
	}

	private static DataStorageClient getDataStorageClient() {
		String dataStorage = PropertyReader.getProperty("dataStorage", DataStorage.RAMDIRECTORY.getValue());
		if (dataStorage.equals(DataStorage.ELASTICSEARCH.getValue())) {
			return ElasticsearchClient.getInstance();
		}
		return RamDirectoryClient.getInstance();
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
	public Response getAutoSuggestions(@RequestParam("field") String field, @RequestParam("term") String term) {
		LOG.debug("field: {}, term: {}", field, term);
		String response = "";
		try {
			response = this.dataStorageClient.getAutoSuggestions(field, term);
		} catch (Exception e) {
			LOG.error("Get auto suggestions failed: " + e);
			return new ResponseEntity<>(beerRating, getStatus(e));
		}
		LOG.debug("Response: {}", response);
		return new ResponseEntity<>(beerRating, HttpStatus.OK);
	}

	@RequestMapping(value = "/getRatings")
	public ResponseEntity<List<BeerRating>> getRatings(@RequestParam("field") String field,
			@RequestParam("term") String term) {
		LOG.debug("field: {}, term: {}", field, term);
		List<BeerRating> response;
		try {
			response = this.dataStorageClient.getBeerRatings(field, term);
		} catch (Exception e) {
			LOG.error("Get ratings failed: " + e);
			return new ResponseEntity<>(beerRating, getStatus(e));
		}
		LOG.debug("Response: {}", response);
		return new ResponseEntity<List<BeerRating>>(beerRatings, HttpStatus.OK);
	}

	private static HttpStatus getStatus(Exception e) {
		return HttpStatus.BAD_REQUEST; // TODO
	}
}
