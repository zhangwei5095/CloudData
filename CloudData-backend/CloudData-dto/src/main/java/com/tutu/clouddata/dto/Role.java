package com.tutu.clouddata.dto;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("role")
public class Role implements Serializable{
	private static final long serialVersionUID = 1969508514906808445L;
	@Id
	private String id;
	private String name;
	public Role(){
		
	}
	public Role(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	@JsonProperty("label")
	public void setName(String name) {
		this.name = name;
	}

}
