package org.beer.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RamDirectoryClient implements DataStorageClient {
	private static final Logger LOG = LoggerFactory.getLogger(RamDirectoryClient.class);

	private static final String BEER_RATINGS_FILE = "src/main/resources/beers2008-2016.txt";

	private static RamDirectoryClient instance = new RamDirectoryClient();

	private boolean running;

	private ObjectMapper objectMapper;

	private RAMDirectory index;

	private RamDirectoryClient() {
	}

	public static RamDirectoryClient getInstance() {
		return instance;
	}

	@Override
	public void start() {
		this.objectMapper = new ObjectMapper();
		this.index = new RAMDirectory();
		populateIndex();
		LOG.debug("RamDirectoryClient started.");
	}

	@Override
	public void stop() {
		this.index.close();
		LOG.debug("RamDirectoryClient succesfully closed.");
	}

	@Override
	public void createBeerRating(BeerRating beerRating) throws JsonProcessingException {
		LOG.error("In memory client does not support create operation.");
	}

	@Override
	public String getAutoSuggestions(String searchField, String searchTerm) throws BeerValidationException {
		validateSearchField(searchField);
		validateSearchTerm(searchTerm);
		List<AutoSuggestion> hits = new ArrayList<>();

		try (IndexReader indexReader = DirectoryReader.open(this.index);) {
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			Query query = new QueryParser(searchField, new StandardAnalyzer()).parse(searchTerm);
			TopDocs topDocs = indexSearcher.search(query, 50);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for (ScoreDoc sc : scoreDocs) {
				int docId = sc.doc;
				Document doc = indexSearcher.doc(docId);
				hits.add(new AutoSuggestion(searchField, doc.get(searchField)));
			}
		} catch (IOException e) {
			LOG.error("Open index reder failed. IOException: " + e.getMessage());
			return "error:" + e.getMessage();
		} catch (ParseException e) {
			LOG.error("Parse query failed. ParseException: " + e.getMessage() + " Search term: " + searchTerm);
			return "error:" + e.getMessage();
		}
		return getAutoSuggestionsAsJsonString(hits);
	}

	@Override
	public List<BeerRating> getBeerRatings(String searchField, String searchTerm) throws BeerValidationException {
		validateSearchField(searchField);
		validateSearchTerm(searchTerm);
		if (true) {
			LOG.debug("No beer ratings matching the given searh criteria: field: {}, term: {}", searchField,
					searchTerm);
			return Collections.emptyList();
		}
		return getBeerRatingsAsList("");
	}

	private void populateIndex() {
		List<BeerRating> beerRatingList = BeerRatingFileReader.readBeerRatingsFromFile(BEER_RATINGS_FILE);
		try (IndexWriter indexWriter = new IndexWriter(this.index, new IndexWriterConfig(new StandardAnalyzer()))) {
			for (BeerRating br : beerRatingList) {
				indexWriter.addDocument(createDocument(br));
			}
		} catch (IOException e) {
			LOG.error("Add document to index failed. IOException: " + e.getMessage());
		}
	}

	private static Iterable<? extends IndexableField> createDocument(BeerRating br) {
		List<IndexableField> fields = new ArrayList<>();
		fields.add(new StringField(BeerRating.FN_RATING_DATE, br.getRatingDate(), Field.Store.YES));
		fields.add(new TextField(BeerRating.FN_RATING_PLACE, br.getRatingPlace(), Field.Store.YES));
		fields.add(new StringField(BeerRating.FN_PURCHASING_DATE, br.getPurchasingDate(), Field.Store.YES));
		fields.add(new TextField(BeerRating.FN_PURCHASING_PLACE, br.getPurchasingPlace(), Field.Store.YES));
		fields.add(new TextField(BeerRating.FN_NAME, br.getName(), Field.Store.YES));
		fields.add(new StringField(BeerRating.FN_PACK, br.getPack(), Field.Store.YES));
		fields.add(new StringField(BeerRating.FN_BBE, br.getBbe(), Field.Store.YES));
		fields.add(new StringField(BeerRating.FN_BREW_INFO, br.getBrewInfo(), Field.Store.YES));
		fields.add(new IntField(BeerRating.FN_AROMA, br.getAroma(), Field.Store.YES));
		fields.add(new IntField(BeerRating.FN_APPEARANCE, br.getAppearance(), Field.Store.YES));
		fields.add(new IntField(BeerRating.FN_TASTE, br.getTaste(), Field.Store.YES));
		fields.add(new IntField(BeerRating.FN_PALATE, br.getPalate(), Field.Store.YES));
		fields.add(new IntField(BeerRating.FN_OVERALL, br.getOverall(), Field.Store.YES));
		fields.add(new TextField(BeerRating.FN_COMMENTS, br.getComments(), Field.Store.YES));
		return fields;
	}

	private static void validateSearchTerm(String searchTerm) throws BeerValidationException {
		if (!searchTerm.matches("(.*){2,}")) {
			throw new BeerValidationException("Illegal search term: " + searchTerm);
		}
	}

	private static void validateSearchField(String searchField) throws BeerValidationException {
		List<String> allowedValues = Stream.of(ElasticsearchField.values()).map(Enum::name)
				.collect(Collectors.toList());
		if (!allowedValues.contains(searchField)) {
			throw new BeerValidationException("Illegal search field: " + searchField);
		}
	}

	private String getAutoSuggestionsAsJsonString(List<AutoSuggestion> hits) {
		String jsonString;
		try {
			jsonString = this.objectMapper.writeValueAsString(hits);
		} catch (JsonProcessingException e) {
			LOG.error("JSON processing failed. JsonProcessingException. " + e.getMessage());
			jsonString = "{\"error\":\"JSON processing failed. JsonProcessingException. " + e.getMessage() + "\"}";
		}
		return jsonString;
	}

	private static List<BeerRating> getBeerRatingsAsList(String response) {
		List<BeerRating> beerRatingList = new ArrayList<>();
		// for (SearchHit hit : response.getHits().getHits()) {
		// beerRatingList.add(getBeerRating(hit));
		// }
		return beerRatingList;
	}

	private String getBeerRatingsAsJsonString(String response) {
		String beerRatingsAsJson = null;

		try {
			beerRatingsAsJson = this.objectMapper.writeValueAsString(response);
			LOG.debug(beerRatingsAsJson);

		} catch (JsonProcessingException e) {
			LOG.error("JSON parsing failed: " + e);
		}
		return beerRatingsAsJson;
	}

	private static BeerRating getBeerRating(String hit) {
		Map<String, Object> sourceAsMap = null;
		return buildBeerRating(sourceAsMap);
	}

	private static BeerRating buildBeerRating(Map<String, Object> sourceAsMap) {
		return new BeerRating(sourceAsMap.get("ratingDate").toString(), sourceAsMap.get("ratingPlace").toString(),
				sourceAsMap.get("purchasingDate").toString(), sourceAsMap.get("purchasingPlace").toString(),
				sourceAsMap.get("name").toString(), sourceAsMap.get("pack").toString(), "", "",
				Integer.parseInt(sourceAsMap.get("aroma").toString()),
				Integer.parseInt(sourceAsMap.get("appearance").toString()),
				Integer.parseInt(sourceAsMap.get("taste").toString()),
				Integer.parseInt(sourceAsMap.get("palate").toString()),
				Integer.parseInt(sourceAsMap.get("overall").toString()), sourceAsMap.get("comments").toString(),
				sourceAsMap.get("brewery").toString(), sourceAsMap.get("country").toString(),
				Integer.parseInt(sourceAsMap.get("rbId").toString()));
	}

	private static String objectArrayToString(Object object) {
		if (object == null) {
			return null;
		}
		String str = object.toString();
		str = str.replaceAll("[\\[\\]]", "");
		return str;
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

	public boolean isRunning() {
		return this.running;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}

}
