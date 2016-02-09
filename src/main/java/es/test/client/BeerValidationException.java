package es.test.client;

import java.util.Map;

public class BeerValidationException extends Exception {

	private static final long serialVersionUID = 1L;
	private Map<String, String> errorMap;

	public BeerValidationException(String message) {
		super(message);
	}

	public BeerValidationException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public BeerValidationException(String message, Map<String, String> errorMap) {
		super(message);
		this.errorMap.putAll(errorMap);
	}

	public BeerValidationException(String message, Throwable throwable, Map<String, String> errorMap) {
		super(message, throwable);
		this.errorMap.putAll(errorMap);
	}
	
	public Map<String, String> getErrorMap() {
		return this.errorMap;
	}
}
