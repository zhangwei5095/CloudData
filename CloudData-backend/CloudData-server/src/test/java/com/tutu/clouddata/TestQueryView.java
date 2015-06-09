package com.tutu.clouddata;

import java.net.UnknownHostException;

import org.bson.BSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.QueryBuilder;
import com.mongodb.util.JSON;
import com.tutu.clouddata.model.MT;

public class TestQueryView {
	public Datastore ds;
	public Mongo mongo;
	@Before
	public void initDs() throws UnknownHostException {
		mongo = new Mongo("localhost", 27017);
		ds = new Morphia().createDatastore(mongo, "c_1");
	}
	@Test
	public void TestQueryByScript(){
		MT mt=ds.find(MT.class).field("_id").equal("customer").get();
		String script=mt.getViews().get(0).getMongoScript();
		DBObject query=(DBObject)JSON.parse(script);
		query.putAll(QueryBuilder.start("create_by").in(new String[]{"1","2"}).get());
		DBCursor cursor=ds.getMongo().getDB("c_1").getCollection("customer").find(query);
		while (cursor.hasNext()) {
			DBObject dbObject = cursor.next();
			System.out.println(dbObject.toMap());
		}
	}
}
