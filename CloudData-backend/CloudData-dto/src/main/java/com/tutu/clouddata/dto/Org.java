package com.tutu.clouddata.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.mongodb.morphia.annotations.Entity;

import com.tutu.clouddata.dto.tree.TreeEntity;

@Entity("org")
public class Org extends TreeEntity<Org> {
	private String parentIds;
	private List<String> children;
	@JsonProperty("label")
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getChildren() {
		return children;
	}

	public void setChildren(List<String> children) {
		this.children = children;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

}
