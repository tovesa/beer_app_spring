package org.beer.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeerApp {

	private static final transient Logger LOG = LoggerFactory.getLogger(BeerApp.class);

	private EmbeddedHttpServer httpServer;

	public static void main(String[] args) {
		BeerApp app = new BeerApp();
		app.start();
	}

	public void start() {
		LOG.debug("Starting BeerApp...");
		this.httpServer = new EmbeddedHttpServer();
		try {
			this.httpServer.start();
		} catch (IllegalStateException e) {
			// stopped, ignore exception
		} catch (Exception e) {
			LOG.error("Start BeerApp failed, exception: ", e);
			stop();
		}

	}

	public void stop() {
		this.httpServer.stop();
		LOG.debug("BeerApp stopped");
	}
}
