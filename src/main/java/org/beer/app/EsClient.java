package org.beer.app;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EsClient implements DataStorageClient {
	private static final transient Logger LOG = LoggerFactory.getLogger(EsClient.class);

	private static EsClient instance = new EsClient();

	private TransportClient transportClient;

	private static final String ES_IP = "localhost";

	private static final int ES_PORT = 9300;

	private static final String ES_CLUSTER_NAME = "es-cluster";

	private static final String ES_INDEX = "beer-rating";

	private static final String ES_TYPE_RATING = "rating";

	private String esSearchScrollExpiry;

	private String esSearchScrollSize;

	private ObjectMapper objectMapper;

	private EsClient() {
	}

	public static EsClient getInstance() {
		return instance;
	}

	@Override
	public void start() {
		LOG.debug("Starting EsClient with values: {}", toString());
		Settings settings = getTransportClientSettings();
		this.transportClient = TransportClient.builder().settings(settings).build();
		this.transportClient.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(ES_IP, ES_PORT)));
		this.objectMapper = new ObjectMapper();
		LOG.debug("EsClient succesfully started.");
	}

	@Override
	public void stop() {
		this.transportClient.close();
		LOG.debug("EsClient succesfully closed.");
	}

	@Override
	public void createBeerRating(BeerRating beerRating) throws JsonProcessingException {
		String ratingAsJson = this.objectMapper.writeValueAsString(beerRating);
		IndexResponse response = this.transportClient.prepareIndex(ES_INDEX, ES_TYPE_RATING).setSource(ratingAsJson)
				.get();

		if (LOG.isDebugEnabled()) {
			logIndexResponse(response);
		}
	}

	@Override
	public String getAutoSuggestions(String searchField, String searchTerm) {

		// TODO remove fixed field
		searchField = "name";

		SearchResponse response = this.transportClient.prepareSearch(ES_INDEX).setTypes(ES_TYPE_RATING)
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(QueryBuilders.termQuery(searchField, searchTerm)).execute().actionGet();

		if (LOG.isDebugEnabled()) {
			logSearchResponse(response);
		}

		String beerRatings = getAutoSuggestionsAsJsonString(response);

		return beerRatings;

	}

	@Override
	public List<BeerRating> getBeerRatings(String searchField, String searchTerm) {

		// TODO remove fixed field
		searchField = "name";

		SearchResponse response = this.transportClient.prepareSearch(ES_INDEX).setTypes(ES_TYPE_RATING)
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(QueryBuilders.termQuery(searchField, searchTerm)).execute().actionGet();

		if (LOG.isDebugEnabled()) {
			logSearchResponse(response);
		}

		List<BeerRating> beerRatings = getBeerRatingsAsList(response);

		return beerRatings;

	}

	private String getAutoSuggestionsAsJsonString(SearchResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<BeerRating> getBeerRatingsAsList(SearchResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	private String getBeerRatingsAsJsonString(SearchResponse response) {
		String beerRatingsAsJson = null;

		try {
			beerRatingsAsJson = this.objectMapper.writeValueAsString(response);
			LOG.debug(beerRatingsAsJson);

		} catch (JsonProcessingException e) {
			LOG.error("JSON parsing failed: " + e);
		}
		return beerRatingsAsJson;
	}

	private static BeerRating getBeerRating(SearchHit hit) {
		Map<String, Object> sourceAsMap = hit.getSource();
		BeerRating beerRating = new BeerRating(sourceAsMap.get("ratingDate").toString(),
				sourceAsMap.get("ratingPlace").toString(), sourceAsMap.get("name").toString(),
				sourceAsMap.get("brewery").toString(), sourceAsMap.get("pack").toString(),
				Integer.parseInt(sourceAsMap.get("aroma").toString()),
				Integer.parseInt(sourceAsMap.get("appearance").toString()),
				Integer.parseInt(sourceAsMap.get("taste").toString()),
				Integer.parseInt(sourceAsMap.get("palate").toString()),
				Integer.parseInt(sourceAsMap.get("overall").toString()), sourceAsMap.get("notes").toString());
		return beerRating;
	}

	private static void logIndexResponse(IndexResponse response) {
		LOG.debug("index:" + response.getIndex());
		LOG.debug("type:" + response.getType());
		LOG.debug("id:" + response.getId());
		LOG.debug("type:" + response.getType());
		LOG.debug("version:" + response.getVersion());
		String operation = (response.isCreated()) ? "create" : "update";
		LOG.debug("operation:" + operation);
	}

	private static void logSearchResponse(SearchResponse response) {
		LOG.debug("search hits: " + response.getHits().hits());
		// TODO fix log
	}

	private static String objectArrayToString(Object object) {
		if (object == null) {
			return null;
		}

		String str = null;
		str = object.toString();
		str = str.replaceAll("[\\[\\]]", "");
		return str;
	}

	private static void handleFailures(BulkResponse bulkResponse) {
		if (bulkResponse.hasFailures()) {
			for (BulkItemResponse bir : bulkResponse.getItems()) {
				if (bir.isFailed()) {
					LOG.error(" ADD SOMETHING Failure message: " + bir.getFailureMessage());
				}
			}
		}
	}

	private Settings getTransportClientSettings() {
		Settings settings = Settings.settingsBuilder().put("cluster.name", ES_CLUSTER_NAME)
				.put("client.transport.sniff", true).build();
		return settings;
	}

	protected void initForIntegrationTest(String esSearchScrollExpiry1, String esSearchScrollSize1) {
		setEsSearchScrollExpiry(esSearchScrollExpiry1);
		setEsSearchScrollSize(esSearchScrollSize1);
		LOG.debug("EsClient initialized for integration test with values: {}", toString());
	}

	protected void initForUnitTest(TransportClient transportClient1, String esSearchScrollExpiry1,
			String esSearchScrollSize1) {
		setTransportClient(transportClient1);
		setEsSearchScrollExpiry(esSearchScrollExpiry1);
		setEsSearchScrollSize(esSearchScrollSize1);
		LOG.debug("EsClient initialized for junit test with values: {}", toString());
	}

	private static void printDebugLog(List<?> list) {
		if (LOG.isDebugEnabled()) {
			int i = 0;
			for (Object o : list) {
				LOG.debug("Item [{}] of List<{}> : {}", Integer.toString(i), o.getClass(), o.toString());
				i++;
			}
		}
	}

	public TransportClient getTransportClient() {
		return this.transportClient;
	}

	public void setTransportClient(TransportClient transportClient) {
		this.transportClient = transportClient;
	}

	public String getEsSearchScrollExpiry() {
		return this.esSearchScrollExpiry;
	}

	public void setEsSearchScrollExpiry(String esSearchScrollExpiry) {
		this.esSearchScrollExpiry = esSearchScrollExpiry;
	}

	public String getEsSearchScrollSize() {
		return this.esSearchScrollSize;
	}

	public void setEsSearchScrollSize(String esSearchScrollSize) {
		this.esSearchScrollSize = esSearchScrollSize;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nesSearchScrollExpiry=");
		sb.append(this.esSearchScrollExpiry);
		sb.append("\nesSearchScrollSize=");
		sb.append(this.esSearchScrollSize);
		return sb.toString();
	}

}
