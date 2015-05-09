package com.tutu.clouddata.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.mongodb.morphia.annotations.Entity;

import com.tutu.clouddata.dto.tree.TreeEntity;

@Entity("org")
public class Org extends TreeEntity<Org> {
	private String parentIds;
	private List<String> childs;
	private String name;
	
	public String getName() {
		return name;
	}
	@JsonProperty("label")
	public void setName(String name) {
		this.name = name;
	}


	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public List<String> getChilds() {
		return childs;
	}

	public void setChilds(List<String> childs) {
		this.childs = childs;
	}

}
