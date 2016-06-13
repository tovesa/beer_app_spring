package org.beer.app;

public class AutoSuggestion {

	public static final String FN_FIELD = "field";
	public static final String FN_VALUE = "value";

	private String field;
	private String value;

	public AutoSuggestion() {
		// default constructor for jackson
	}

	public AutoSuggestion(String field, String value) {
		this.field = field;
		this.value = value;
	}

	public String getField() {
		return this.field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(FN_FIELD);
		sb.append("=");
		sb.append(this.field);
		sb.append(", ");
		sb.append(FN_VALUE);
		sb.append("=");
		sb.append(this.value);
		return sb.toString();
	}
}
