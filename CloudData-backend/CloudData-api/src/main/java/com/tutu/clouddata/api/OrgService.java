package com.tutu.clouddata.api;

import java.util.List;

import com.tutu.clouddata.dto.Org;

public interface OrgService {
	public List<Org> list();
	
	public void addOrg(String name,String parentId);
}
