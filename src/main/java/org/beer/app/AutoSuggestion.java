package org.beer.app;

public class AutoSuggestion {
	private String field;
	private String value;

	public AutoSuggestion(String field, String value) {
		this.field = field;
		this.value = value;
	}

	public String getField() {
		return this.field;
	}

	public void setNasetFieldme(String field) {
		this.field = field;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
