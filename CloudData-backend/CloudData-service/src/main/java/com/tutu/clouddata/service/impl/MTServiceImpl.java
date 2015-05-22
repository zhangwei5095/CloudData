package com.tutu.clouddata.service.impl;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.tutu.clouddata.api.MTService;
import com.tutu.clouddata.model.MF;
import com.tutu.clouddata.model.MFJsonViews;
import com.tutu.clouddata.model.MFText;
import com.tutu.clouddata.model.MT;
import com.tutu.clouddata.service.BasicService;

@Service("mtService")
@Path("/mt")
public class MTServiceImpl extends BasicService implements MTService {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<MT> list() {
		return getDataStore().find(MT.class).asList();
	}

	public MT mt( String mid) {
		return getDataStore().find(MT.class).field("id").equal(mid).get();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void save(MT mt) {
		getDataStore().save(mt);
	}
	
	@Path("mf")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void addMf(MFJsonViews mf,@QueryParam("mtid") String mtid) {
		MF rmf=new MFText();
		BeanUtils.copyProperties(mf, rmf);
		UpdateOperations<MT> ops;
		Query<MT> updateQuery = getDataStore().createQuery(MT.class).field("_id").equal(mtid);
		ops = getDataStore().createUpdateOperations(MT.class).add("mfs", rmf);
		getDataStore().update(updateQuery, ops);
	}

}
