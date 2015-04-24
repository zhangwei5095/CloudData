package com.tutu.clouddata.service;

import org.mongodb.morphia.Datastore;

import com.mongodb.DBCollection;
import com.tutu.clouddata.context.ContextHolder;

public class BasicService {
	public Datastore getDataStore(){
		return ContextHolder.getContext().getDatastore();
	}
	
	public DBCollection getCollection(String collName){
		return getDataStore().getDB().getCollection(collName);
	}
}
