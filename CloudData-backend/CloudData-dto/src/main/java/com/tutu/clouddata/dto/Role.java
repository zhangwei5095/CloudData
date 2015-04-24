package com.tutu.clouddata.dto;

import org.codehaus.jackson.annotate.JsonProperty;
import org.mongodb.morphia.annotations.Id;

public class Role {
	@Id
	private String id;
	@JsonProperty("label")
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
