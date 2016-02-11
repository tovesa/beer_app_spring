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

import org.elasticsearch.index.IndexNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/ratingService")
public class BeerRatingService {

	private static final transient Logger LOG = LoggerFactory.getLogger(BeerRatingService.class);
	private EsClient esClient;

	public BeerRatingService() {
		this.esClient = EsClient.getInstance();
		this.esClient.start();
	}

	@POST
	@Path("createRating")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createRating(BeerRating beerRating) {
		LOG.debug("createRating: " + beerRating.toString());
		try {
			this.esClient.createBeerRating(beerRating);
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
		LOG.debug("getAutoSuggestions: field:" + field + ", term:" + term);
		String response = "";
		try {
			response = this.esClient.getAutoSuggestions(field, term);
		} catch (Exception e) {
			LOG.error("Get auto suggestions failed: " + e);
			int status = 500; // TODO
			return Response.status(status).entity(response).build();
		}
		return Response.status(200).entity(response).build();
	}

	@GET
	@Path("getRatings")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRatings(@QueryParam("field") String field, @QueryParam("term") String term) {
		LOG.debug("getRatings: field:" + field + ", term:" + term);
		List<BeerRating> response;
		try {
			response = this.esClient.getBeerRatings(field, term);
		} catch (IndexNotFoundException e) {
			LOG.error("Index not found, exception: " + e);
			int status = 500; // TODO
			return Response.status(status).entity(e).build();
		}
		return Response.status(200).entity(response).build();
	}

	@POST
	@Path("close")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response close() {
		this.esClient.stop();
		return Response.status(200).entity("Elasticsearch connection closed.").build();
	}
}
