package org.beer.app.dao;

import java.util.List;

import org.beer.app.BeerRating;
import org.beer.app.BeerValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDbClient implements DataStorageClient {

	private static final Logger LOG = LoggerFactory.getLogger(MongoDbClient.class);
	private static MongoDbClient instance = new MongoDbClient();
	private MongoClient mongoClient;
	private MongoDatabase mongoDb;

	private MongoDbClient() {
	}

	public static MongoDbClient getInstance() {
		return instance;
	}

	@Override
	public void start() {
		this.mongoClient = new MongoClient("localhost");
		this.mongoDb = this.mongoClient.getDatabase("beerDb");
		LOG.debug("MongoDbClient started.");

	}

	@Override
	public void stop() {
		this.mongoClient.close();
		LOG.debug("MongoDbClient succesfully closed.");

	}

	@Override
	public void createBeerRating(BeerRating beerRating) throws JsonProcessingException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAutoSuggestions(String searchField, String searchTerm) throws BeerValidationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BeerRating> getBeerRatings(String searchField, String searchTerm) throws BeerValidationException {
		// TODO Auto-generated method stub
		return null;
	}
}
