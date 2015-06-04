package com.tutu.clouddata.dto.datatable;

import java.util.List;
import java.util.Map;

public class DataTableDTO {
	private Integer draw;
	private Long recordsTotal;
	private Long recordsFiltered;
	private List<Map<String,String>> data;
	private String error;
	public Integer getDraw() {
		return draw;
	}
	public void setDraw(Integer draw) {
		this.draw = draw;
	}
	public Long getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(Long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public Long getRecordsFiltered() {
		return recordsFiltered;
	}
	public void setRecordsFiltered(Long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
	public List<Map<String,String>> getData() {
		return data;
	}
	public void setData(List<Map<String,String>> data) {
		this.data = data;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
}
