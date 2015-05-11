package com.tutu.clouddata.api;

import java.util.List;

import com.tutu.clouddata.dto.Role;
import com.tutu.clouddata.dto.RoleMT;

public interface RoleService {
	List<Role> list();

	List<RoleMT> listMT(String roleId);
}
