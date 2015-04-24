package com.tutu.clouddata.dto;

public enum ResourceType {
	MENU("menu");
	private final String type;

	ResourceType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
