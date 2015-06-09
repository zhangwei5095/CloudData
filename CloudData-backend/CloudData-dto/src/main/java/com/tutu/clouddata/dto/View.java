package com.tutu.clouddata.dto;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;


public class View {
	@Id
	private ObjectId id;
	private String viewName;
	private String rules;
	private String mongoScript;
	private String displayColumn;
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	public String getRules() {
		return rules;
	}
	public void setRules(String rules) {
		this.rules = rules;
	}
	public String getMongoScript() {
		return mongoScript;
	}
	public void setMongoScript(String mongoScript) {
		this.mongoScript = mongoScript;
	}
	public String getDisplayColumn() {
		return displayColumn;
	}
	public void setDisplayColumn(String displayColumn) {
		this.displayColumn = displayColumn;
	}
}
