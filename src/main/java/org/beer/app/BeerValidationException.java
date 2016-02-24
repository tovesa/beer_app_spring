package org.beer.app;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BeerValidationException extends Exception {

	private static final long serialVersionUID = 1L;
	private final Map<String, String> errorMap;

	public BeerValidationException(String message) {
		super(message);
		this.errorMap = Collections.emptyMap();
	}

	public BeerValidationException(String message, Throwable throwable) {
		super(message, throwable);
		this.errorMap = Collections.emptyMap();
	}

	public BeerValidationException(String message, Map<String, String> errorMap) {
		super(message);
		this.errorMap = new HashMap<>();
		this.errorMap.putAll(errorMap);
	}

	public BeerValidationException(String message, Throwable throwable, Map<String, String> errorMap) {
		super(message, throwable);
		this.errorMap = new HashMap<>();
		this.errorMap.putAll(errorMap);
	}

	public Map<String, String> getErrorMap() {
		return this.errorMap;
	}
}
