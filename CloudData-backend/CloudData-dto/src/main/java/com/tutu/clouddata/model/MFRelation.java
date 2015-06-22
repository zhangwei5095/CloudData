package com.tutu.clouddata.model;

import org.codehaus.jackson.annotate.JsonIgnore;

public class MFRelation extends MF {
	private String relationObj;

	String getType() {
		return FieldType.RELATION.getType();
	}

	@JsonIgnore(true)
	public Object getRawValue() {
		return null;
	}

	public String getRelationObj() {
		return relationObj;
	}

	public void setRelationObj(String relationObj) {
		this.relationObj = relationObj;
	}

}
