package org.beer.app;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/ws")
public class BeerRatingService {

	private static final Logger LOG = LoggerFactory.getLogger(BeerRatingService.class);
	private DataStorageClient dataStorageClient;

	public BeerRatingService() {
		this.dataStorageClient = getDataStorageClient();
		this.dataStorageClient.start();
	}

	private static DataStorageClient getDataStorageClient() {
		String dataStorage = PropertyReader.getProperty("dataStorage", DataStorage.RAMDISC.getValue());
		if (dataStorage.equals(DataStorage.ELASTICSEARCH.getValue())) {
			return ElasticsearchClient.getInstance();
		}
		return InMemoryClient.getInstance();
	}

	@POST
	@Path("createRating")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createRating(BeerRating beerRating) {
		LOG.debug("createRating: " + beerRating.toString());
		try {
			this.dataStorageClient.createBeerRating(beerRating);
		} catch (Exception e) {
			LOG.error("Create rating failed: " + e);
			int status = 500; // TODO
			return Response.status(status).entity(e).build();
		}
		return Response.status(200).entity("Beer rating succesfully created.").build();
	}

	@GET
	@Path("getAutoSuggestions")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAutoSuggestions(@QueryParam("field") String field, @QueryParam("term") String term) {
		LOG.debug("field: {}, term: {}", field, term);
		String response = "";
		try {
			response = this.dataStorageClient.getAutoSuggestions(field, term);
		} catch (Exception e) {
			LOG.error("Get auto suggestions failed: " + e);
			int status = 500; // TODO
			return Response.status(status).entity(response).build();
		}
		LOG.debug("Response: {}", response);
		return Response.status(200).entity(response).build();
	}

	@GET
	@Path("getRatings")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRatings(@QueryParam("field") String field, @QueryParam("term") String term) {
		LOG.debug("field: {}, term: {}", field, term);
		List<BeerRating> response;
		try {
			response = this.dataStorageClient.getBeerRatings(field, term);
		} catch (Exception e) {
			LOG.error("Get ratings failed: " + e);
			int status = 500; // TODO
			return Response.status(status).entity(e).build();
		}
		LOG.debug("Response: {}", response);
		return Response.status(200).entity(response).build();
	}

	@POST
	@Path("close")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response close() {
		this.dataStorageClient.stop();
		return Response.status(200).entity("Elasticsearch connection closed.").build();
	}
}
