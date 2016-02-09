package es.test.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeerApp {

	private static final transient Logger LOG = LoggerFactory.getLogger(BeerApp.class);

	public static void main(String[] args) {
		EmbeddedHttpServer httpServer = new EmbeddedHttpServer();
		EsClient esClient = new EsClient();
		// HttpClient httpClient = new HttpClient();
		try {
			httpServer.start();
			esClient.start();
		} catch (Exception e) {
			LOG.error("Exception: " + e);
		}

		// TODO stop services
	}
}
