package com.tutu.clouderp.api;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface DataService {
	void delete(String tid, String id);

	void create(String mid, HttpServletRequest request);

	List<Map<String,Object>> read(String collectionName, Integer page, Integer pageSize);
}
