package com.tutu.clouddata.dto;

import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("role_mts")
public class RoleMTS {
	@Id
	private String roleId;
	private List<RoleMT> roleMTs;
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public List<RoleMT> getRoleMTs() {
		return roleMTs;
	}
	public void setRoleMTs(List<RoleMT> roleMTs) {
		this.roleMTs = roleMTs;
	}
}
