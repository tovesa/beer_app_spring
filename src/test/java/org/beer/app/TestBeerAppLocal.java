package org.beer.app;

import org.beer.app.BeerApp;
import org.junit.Test;

public class TestBeerAppLocal {

	@Test
	public void testStartBeerApp() {
		BeerApp app = new BeerApp();
		app.start();
	}
}
