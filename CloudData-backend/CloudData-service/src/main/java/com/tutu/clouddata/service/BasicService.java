package com.tutu.clouddata.service;

import java.util.List;

import org.mongodb.morphia.Datastore;

import com.mongodb.DBCollection;
import com.tutu.clouddata.context.ContextHolder;
import com.tutu.clouddata.dto.Org;
import com.tutu.clouddata.dto.auth.User;

public class BasicService {
	public Datastore getDataStore() {
		return ContextHolder.getContext().getDatastore();
	}

	public User getUser() {
		return ContextHolder.getContext().getUser();
	}

	public DBCollection getCollection(String collName) {
		return getDataStore().getDB().getCollection(collName);
	}

	public String[] getChildUserIds() {
		Org org = getDataStore().find(Org.class).field("id").equal(getUser().getOrgId()).get();
		List<String> childOrgs = org.getChilds();
		String[] orgs = childOrgs.toArray(new String[childOrgs.size()]);
		List<User> users = getDataStore().find(User.class, "orgId", orgs).asList();
		String[] userIds = new String[users.size()];
		for (int i = 0; i < users.size(); i++) {
			userIds[i] = users.get(i).getName();
		}
		return userIds;
	}
}
