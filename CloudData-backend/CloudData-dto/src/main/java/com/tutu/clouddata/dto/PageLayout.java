package com.tutu.clouddata.dto;

import org.mongodb.morphia.annotations.Reference;

import com.tutu.clouddata.model.MT;

public class PageLayout {
	@Reference
	private MT mt;

	public MT getMt() {
		return mt;
	}

	public void setMt(MT mt) {
		this.mt = mt;
	}
}
