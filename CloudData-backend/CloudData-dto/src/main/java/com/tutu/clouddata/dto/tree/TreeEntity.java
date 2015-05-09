package com.tutu.clouddata.dto.tree;

import java.util.List;

public class TreeEntity<T> {
	private String id;
	private String pid;
	private List<TreeEntity<T>> children;

	public TreeEntity() {
	}

	public TreeEntity(String id, String pid, String text, String url) {
		super();
		this.id = id;
		this.pid = pid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public List<TreeEntity<T>> getChildren() {
		return children;
	}

	public void setChildren(List<TreeEntity<T>> children) {
		this.children = children;
	}

}
