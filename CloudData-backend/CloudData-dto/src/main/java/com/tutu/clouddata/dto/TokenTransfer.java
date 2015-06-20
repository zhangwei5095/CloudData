package com.tutu.clouddata.dto;

public class TokenTransfer {

	private final String token;

	private String name;

	private String[] roles;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	public TokenTransfer(String token) {
		this.token = token;
	}

	public String getToken() {
		return this.token;
	}

}