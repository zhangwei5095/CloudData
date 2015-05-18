package com.tutu.clouddata.service.impl;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tutu.clouddata.api.RoleService;
import com.tutu.clouddata.dto.Role;
import com.tutu.clouddata.dto.RoleMT;
import com.tutu.clouddata.dto.RoleMTS;
import com.tutu.clouddata.service.BasicService;

@Service("roleService")
@Path("/role")
public class RoleServiceImpl extends BasicService implements RoleService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Role> list() {
		return getDataStore().find(Role.class).asList();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String save(Role role) {
		return getDataStore().save(role).getId().toString();
	}
	
	@GET
	@Path("mt")
	@Produces(MediaType.APPLICATION_JSON)
	public List<RoleMT> listMT(@QueryParam("roleId") String roleId) {
		RoleMTS roleMts = getDataStore().find(RoleMTS.class).field("roleId").equal(roleId).get();
		return roleMts == null ? null : roleMts.getRoleMTs();
	}

	@POST
	@Path("mt")
	@Consumes(MediaType.APPLICATION_JSON)
	public void saveRoleMT(RoleMTS roleMTs) {
		getDataStore().delete(RoleMTS.class, roleMTs.getRoleId());
		getDataStore().save(roleMTs);
	}
}
