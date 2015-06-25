package com.tutu.clouddata.api;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.tutu.clouddata.dto.datatable.DataTableDTO;

public interface DataService {
	void delete(String tid, String id);

	void save(String mid,String rid, HttpServletRequest request);
	
	Map<String, Object> read(String mid,String rid);
	
	List<Map<String, Object>> readNgGrid(String collectionName, Integer page, Integer pageSize);

	List<Map<String, Object>> readDataByVid(String collectionName, String vid, Integer page, Integer pageSize);

	DataTableDTO readDataTable(String collectionName, Integer page, Integer pageSize);
}
