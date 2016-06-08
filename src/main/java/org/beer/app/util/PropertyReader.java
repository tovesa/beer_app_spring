package org.beer.app.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.beer.app.BeerRatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyReader {

	private static final Logger LOG = LoggerFactory.getLogger(BeerRatingService.class);

	private static final String PROPERTIES_FILE = "src/main/resources/config.properties";

	private static PropertyReader instance = new PropertyReader();

	private static Properties properties;

	private PropertyReader() {
	}

	public static PropertyReader getInstance() {
		loadProperties();
		return instance;
	}

	public static String getProperty(String propertyName, String defaultValue) {
		return properties.getProperty(propertyName, defaultValue);
	}

	private static Properties loadProperties() {
		properties = new Properties();
		try (FileReader fs = new FileReader(new File(PROPERTIES_FILE))) {
			properties.load(fs);
		} catch (IOException e) {
			LOG.error("Loading properties from file {} failed. Default values are used.", PROPERTIES_FILE);
		}
		return properties;
	}
}
