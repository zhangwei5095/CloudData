package com.tutu.clouddata.model;

import org.codehaus.jackson.annotate.JsonIgnore;

public class MFRelation extends MF {
	private String relationObj;

	public String getType() {
		return FieldType.RELATION.getType();
	}

	@JsonIgnore(true)
	public Object getRawValue() {
		return stringValue;
	}

	public String getRelationObj() {
		return relationObj;
	}

	public void setRelationObj(String relationObj) {
		this.relationObj = relationObj;
	}

}
