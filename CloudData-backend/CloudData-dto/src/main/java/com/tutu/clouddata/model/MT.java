package com.tutu.clouddata.model;

import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.tutu.clouddata.dto.View;

/**
 * 元数据表
 * 
 * @Title MT.java
 * @Package com.passionguy.force.dto
 * @Description
 * @author tutu
 * @date 2014-11-20
 */
@Entity("mt")
public class MT {
	@Id
	private String id;
	private String name;
	private List<MF> mfs;
	private List<View> views;
	private List<Relation> relationObjs; 
	public MT() {
	}

	public MT(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MF> getMfs() {
		return mfs;
	}

	public void setMfs(List<MF> mfs) {
		this.mfs = mfs;
	}

	public List<View> getViews() {
		return views;
	}

	public void setViews(List<View> views) {
		this.views = views;
	}

	public List<Relation> getRelationObjs() {
		return relationObjs;
	}

	public void setRelationObjs(List<Relation> relationObjs) {
		this.relationObjs = relationObjs;
	}

}
