package com.tutu.clouddata.model;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public enum FieldType {
	TEXT("text"),
	EMAIL("email"),
	URL("url"),
	NUMBER("number"),
	DATE("date"),
	TIME("time"),
	SELECT("select"),
	MULTISELECT("multiselect"),
	RELATION("relation"),
	AUTOCODE("autocode"),
	CHECKBOX("checkbox");
	private final String type;

	FieldType(String type) {
		this.type = type;
	}
	@JsonProperty("type")
	public String getType() {
		return type;
	}
	
	@JsonCreator
	public static FieldType fromType(String type) {
	    for (FieldType c: FieldType.values()) {
	        if (c.type.equals(type)) {
	            return c;
	        }
	    }
	    throw new IllegalArgumentException("Invalid Status type code: " + type);        
	}
}
