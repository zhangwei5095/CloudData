package com.tutu.clouddata.api;

import java.util.List;

import com.tutu.clouddata.model.MT;

public interface MTService {
	public List<MT> list();
	public MT mt(String mid);
}
