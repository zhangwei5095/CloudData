package com.tutu.clouddata.api;

import java.util.List;

import com.tutu.clouddata.dto.Role;
import com.tutu.clouddata.dto.RoleMT;
import com.tutu.clouddata.dto.RoleMTS;

public interface RoleService {
	List<Role> list();

	List<RoleMT> listMT(String roleId);

	String save(Role role);

	void saveRoleMT(RoleMTS roleMTs);
}
