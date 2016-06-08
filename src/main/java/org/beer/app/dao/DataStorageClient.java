package org.beer.app.dao;

import java.util.List;

import org.beer.app.BeerRating;
import org.beer.app.BeerValidationException;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface DataStorageClient {
	public void start();

	public void stop();

	public void createBeerRating(BeerRating beerRating) throws JsonProcessingException;

	public String getAutoSuggestions(String searchField, String searchTerm) throws BeerValidationException;

	public List<BeerRating> getBeerRatings(String searchField, String searchTerm) throws BeerValidationException;
}
