package com.tutu.clouddata.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.common.json.ParseException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.util.JSON;
import com.tutu.clouddata.api.DataService;
import com.tutu.clouddata.api.MTService;
import com.tutu.clouddata.context.ContextHolder;
import com.tutu.clouddata.dto.View;
import com.tutu.clouddata.dto.datatable.DataTableDTO;
import com.tutu.clouddata.model.MF;
import com.tutu.clouddata.model.MT;
import com.tutu.clouddata.service.BasicService;

@Service("dataService")
@Path("/data")
public class DataServiceImpl extends BasicService implements DataService {
	private static Logger logger = LoggerFactory.getLogger(DataServiceImpl.class);
	@Resource
	private MTService mtService;

	@Override
	public void delete(String tid, String id) {
		BasicDBObject query = new BasicDBObject("id", new ObjectId(id));
		getCollection(tid).findAndRemove(query);
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("/c")
	@Consumes(MediaType.APPLICATION_JSON)
	public void save(@QueryParam("mid") String mid, @QueryParam("rid") String rid, @Context HttpServletRequest request) {
		Map<String, String> postData = null;
		try {
			postData = com.alibaba.dubbo.common.json.JSON.parse(request.getReader(), Map.class);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		MT mt = mtService.mt(mid);
		if (StringUtils.isEmpty(rid)) {
			save(mt, postData);
		} else {
			update(mt, rid, postData);
		}
		logger.debug(postData.toString());
	}

	@GET
	@Path("/rg")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Map<String, Object>> readNgGrid(@QueryParam("mid") String mid, @QueryParam("page") Integer page,
			@QueryParam("pagesize") Integer pagesize) {
		return getData(mid, page, pagesize);
	}

	public DataTableDTO readDataTable(@QueryParam("mid") String mid, @QueryParam("page") Integer page,
			@QueryParam("pagesize") Integer pagesize) {
		return getDataTableData(mid, 1, 10);
	}

	private DataTableDTO getDataTableData(String collectionName, int page, int pagesize) {
		DataTableDTO dataTableDTO = new DataTableDTO();
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> rowData;
		int skip = (page - 1) * pagesize;
		dataTableDTO.setRecordsTotal(getCollection(collectionName).count());
		DBCursor dbCursor = getCollection(collectionName).find().sort(null).skip(skip).limit(pagesize);
		Set<String> keys;
		while (dbCursor.hasNext()) {
			rowData = new HashMap<String, String>();
			DBObject dbObject = dbCursor.next();
			keys = dbObject.keySet();
			for (String key : keys) {
				rowData.put(key, String.valueOf(dbObject.get(key)));
			}
			data.add(rowData);
		}
		dataTableDTO.setData(data);
		return dataTableDTO;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getData(String collectionName, int page, int pagesize) {
		List<Map<String, Object>> data = new ArrayList<>();
		int skip = (page - 1) * pagesize;
		DBCursor dbCursor = getCollection(collectionName).find().sort(null).skip(skip).limit(pagesize);
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
		dbObject.put("create_by", ContextHolder.getContext().getUser().getName());
		dbObject.put("create_at", new Date());
		for (MF mf : mt.getMfs()) {
			mf.setStringValue(String.valueOf(dataMap.get(mf.getKey())));
			dbObject.put(mf.getKey(), mf.getRawValue());
		}
		getCollection(mt.getId()).save(dbObject);
	}

	private void update(MT mt, String rid, Map<String, String> dataMap) {
		DBObject q = new BasicDBObject();
		q.put("_id", new ObjectId(rid));
		DBObject dbObject = new BasicDBObject();
		dbObject.put("update_by", ContextHolder.getContext().getUser().getName());
		dbObject.put("update_at", new Date());
		for (MF mf : mt.getMfs()) {
			mf.setStringValue(String.valueOf(dataMap.get(mf.getKey())));
			dbObject.put(mf.getKey(), mf.getRawValue());
		}
		getCollection(mt.getId()).update(q, dbObject);
	}

	@GET
	@Path("/rv")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Map<String, Object>> readDataByVid(@QueryParam("mid") String collectionName,
			@QueryParam("vid") String vid, @QueryParam("page") Integer page, @QueryParam("pagesize") Integer pageSize) {
		List<Map<String, Object>> data = new ArrayList<>();
		View view = getView(collectionName, vid);
		int skip = (page - 1) * pageSize;
		String[] childUserIds = getChildUserIds();
		DBObject query = null;
		query = (DBObject) JSON.parse(view.getMongoScript());
		query.putAll(QueryBuilder.start("create_by").in(childUserIds).get());
		DBCursor cursor = getCollection(collectionName).find(query).skip(skip).limit(pageSize);
		while (cursor.hasNext()) {
			DBObject dbObject = cursor.next();
			dbObject.put("_id", dbObject.get("_id").toString());
			data.add(dbObject.toMap());
		}
		return data;
	}

	private View getView(String collectionName, String vid) {
		MT mt = getDataStore().find(MT.class).field("_id").equal(collectionName).get();
		for (View view : mt.getViews()) {
			if (vid.equals(view.getId().toString()))
				return view;
		}
		return null;
	}

	@GET
	@Path("/r")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> read(@QueryParam("mid") String mid, @QueryParam("rid") String rid) {
		DBObject dbObject = getCollection(mid).findOne(new ObjectId(rid));
		return dbObject.toMap();
	}

}
