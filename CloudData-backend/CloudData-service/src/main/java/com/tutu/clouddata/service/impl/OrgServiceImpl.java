package com.tutu.clouddata.service.impl;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.tutu.clouddata.api.OrgService;
import com.tutu.clouddata.dto.Org;
import com.tutu.clouddata.service.BasicService;
@Service("orgService")
@Path("/org")
public class OrgServiceImpl extends BasicService implements OrgService{
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Org> list() {
		return getDataStore().find(Org.class).asList();
	}

	@Override
	public void addOrg(String name, String parentId) {
		
		
	}

}
