package org.beer.app;

import org.junit.Test;

public class TestRbClient {

	@Test
	public void testGetRbId() {
		RbClient client = new RbClient();
		String beerName = "Olvi APA";
		String rbId = client.getRbId(beerName);
		System.out.println("RbId: " + rbId);
	}
}
