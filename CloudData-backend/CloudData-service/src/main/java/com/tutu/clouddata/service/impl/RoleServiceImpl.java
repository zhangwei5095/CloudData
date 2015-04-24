package com.tutu.clouddata.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tutu.clouddata.api.RoleService;
import com.tutu.clouddata.auth.dao.SystemDatastore;
import com.tutu.clouddata.dto.Role;

@Service("roleService")
@Path("/role")
public class RoleServiceImpl implements RoleService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private SystemDatastore systemDatastore;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Role> list() {
		return systemDatastore.find(Role.class).asList();
	}

	
}
