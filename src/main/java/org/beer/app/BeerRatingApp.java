package org.beer.app;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BeerRatingApp {

	private static final Logger LOG = LoggerFactory.getLogger(BeerRatingApp.class);

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(BeerRatingApp.class, args);
		if (LOG.isDebugEnabled()) {
			List<String> beans = Arrays.asList(ctx.getBeanDefinitionNames());
			beans.sort(Comparator.naturalOrder());
			beans.forEach(LOG::debug);
		}
	}
}
