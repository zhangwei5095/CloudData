package com.tutu.clouddata.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.mongodb.BasicDBObject;
import com.tutu.clouddata.api.MTService;
import com.tutu.clouddata.context.ContextHolder;
import com.tutu.clouddata.dto.View;
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
		List<MT> mts = getDataStore().find(MT.class).asList();
		for (MT mt : mts) {
			List<View> views = mt.getViews();
			for (Iterator<View> iter = views.iterator(); iter.hasNext();) {
				View view = iter.next();
				if (!view.getCreator().equals(
						ContextHolder.getContext().getUser()))
					iter.remove();
			}
		}
		return mts;
	}

	public MT mt(String mid) {
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
	public void addMf(MFJsonViews mf, @QueryParam("mtid") String mtid) {
		MF rmf = new MFText();
		BeanUtils.copyProperties(mf, rmf);
		UpdateOperations<MT> ops;
		Query<MT> updateQuery = getDataStore().createQuery(MT.class)
				.field("_id").equal(mtid);
		ops = getDataStore().createUpdateOperations(MT.class).add("mfs", rmf);
		getDataStore().update(updateQuery, ops);
	}

	@Path("view")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void saveView(View view, @QueryParam("mtid") String mtid,
			@QueryParam("vid") String vid) {
		view.setCreateDate(new Date());
		view.setCreator(ContextHolder.getContext().getUser());
		if (StringUtils.isEmpty(vid)) {
			view.setId(new ObjectId().toString());
			UpdateOperations<MT> ops;
			Query<MT> updateQuery = getDataStore().createQuery(MT.class)
					.field("_id").equal(mtid);
			ops = getDataStore().createUpdateOperations(MT.class).add("views",
					view);
			getDataStore().update(updateQuery, ops);
		} else {
			view.setId(vid);
			Query<MT> updateQuery = getDataStore().createQuery(MT.class)
					.field("_id").equal(mtid);
			UpdateOperations<MT> ops = getDataStore().createUpdateOperations(MT.class).removeAll("views", new BasicDBObject("_id", vid)); 
			getDataStore().update(updateQuery,ops);
			UpdateOperations<MT> ops2 = getDataStore().createUpdateOperations(MT.class).add("views", view); 
			getDataStore().update(updateQuery,ops2);
		}
	}
	
	@Path("delete/mt/{mtid}/view/{vid}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteView(@PathParam("mtid") String mtid,
			@PathParam("vid") String vid) {
		Query<MT> updateQuery = getDataStore().createQuery(MT.class)
				.field("_id").equal(mtid);
		UpdateOperations<MT> ops = getDataStore().createUpdateOperations(
				MT.class).removeAll("views", new BasicDBObject("_id", vid));
		getDataStore().update(updateQuery, ops);
	}
}
