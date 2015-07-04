package com.tutu.clouddata.service.impl;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tutu.clouddata.api.OrgService;
import com.tutu.clouddata.dto.Org;
import com.tutu.clouddata.dto.tree.TreeBuilder;
import com.tutu.clouddata.service.BasicService;

@Service("orgService")
@Path("/org")
public class OrgServiceImpl extends BasicService implements OrgService {
	@SuppressWarnings("unchecked")
	@GET
	@Path("/listTree")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Org> listTree() {
		List<Org> orgs = getDataStore().find(Org.class).asList();
		@SuppressWarnings("rawtypes")
		TreeBuilder treeBuilder = new TreeBuilder(orgs);
		List<Org> orgTree = treeBuilder.buildTree();
		return orgTree;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Org> list() {
		return getDataStore().find(Org.class).asList();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void addOrg(Org org) {
		getDataStore().save(org);
		Org porg = getDataStore().createQuery(Org.class).field("id")
				.equal(org.getPid()).get();
		while (!StringUtils.isEmpty(porg.getPid())) {
			Query<Org> updateQuery = getDataStore().createQuery(Org.class)
					.field("id").equal(porg.getPid());
			UpdateOperations<Org> ops = getDataStore().createUpdateOperations(
					Org.class).add("childs", org.getId());
			getDataStore().update(updateQuery, ops);
			porg = getDataStore().createQuery(Org.class).field("id")
					.equal(porg.getPid()).get();
		}
	}

}
