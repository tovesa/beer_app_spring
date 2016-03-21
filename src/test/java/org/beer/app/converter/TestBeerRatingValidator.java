package org.beer.app.converter;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TestBeerRatingValidator {

	@Test
	public void TestValidate_invalidMonth() {
		String line = "2014-14-05. Pirkkala. . Bierkompass. Jacks Abby Smoke & Dagger. Bottle 355ml. . . Smoke, dark chocolate, roasted barley, mild coffee, smooth palate, med carbo. 738315.";
		assertFalse(BeerRatingValidator.isValid(line, 1));
	}
}
