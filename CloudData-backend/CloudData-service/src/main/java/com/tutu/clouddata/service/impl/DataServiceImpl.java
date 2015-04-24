package com.tutu.clouddata.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.ParseException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.tutu.clouddata.api.DataService;
import com.tutu.clouddata.api.MTService;
import com.tutu.clouddata.context.ContextHolder;
import com.tutu.clouddata.model.MF;
import com.tutu.clouddata.model.MT;

@Service("dataService")
@Path("/data")
public class DataServiceImpl implements DataService {
	private static Logger logger = LoggerFactory
			.getLogger(DataServiceImpl.class);
	@Resource
	private MTService mtService;

	private DBCollection getCollection(String collectionName) {
		return ContextHolder.getContext().getDatastore().getDB()
				.getCollection(collectionName);
	}

	@Override
	public void delete(String tid, String id) {
		BasicDBObject query = new BasicDBObject("id", new ObjectId(id));
		getCollection(tid).findAndRemove(query);
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("/c")
	@Consumes(MediaType.APPLICATION_JSON)
	public void create(@QueryParam("mid") String mid,
			@Context HttpServletRequest request) {
		Map<String, String> postData = null;
		try {
			postData = JSON.parse(request.getReader(), Map.class);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		MT mt = mtService.mt(mid);
		save(mt, postData);
		logger.debug(postData.toString());
	}

	@GET
	@Path("/r")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Map<String,Object>> read(@QueryParam("mid") String mid,
			@QueryParam("page") Integer page,
			@QueryParam("pagesize") Integer pagesize) {
		return getData(mid, 1, 10);
	}

	public List<Map<String,Object>> getData(String collectionName, int page, int pagesize) {
		List<Map<String,Object>> data = new ArrayList<>();
		int skip = (page - 1) * pagesize;
		DBCursor dbCursor = getCollection(collectionName).find().sort(null)
				.skip(skip).limit(pagesize);
		while (dbCursor.hasNext()) {
			DBObject dbObject = dbCursor.next();
			dbObject.put("_id", dbObject.get("_id").toString());
			
			data.add(dbObject.toMap());
		}
		logger.debug(data.toString());
		return data;
	}

	private void save(MT mt, Map<String, String> dataMap) {
		DBObject dbObject = new BasicDBObject();
		for (MF mf : mt.getMfs()) {
			mf.setStringValue(String.valueOf(dataMap.get(mf.getKey())));
			dbObject.put(mf.getKey(), mf.getRawValue());
		}
		getCollection(mt.getId()).save(dbObject);
	}
}
