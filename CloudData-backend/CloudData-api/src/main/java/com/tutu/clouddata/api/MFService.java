package com.tutu.clouddata.api;

import java.util.List;

import com.tutu.clouddata.model.MF;

public interface MFService {
	public List<MF> list(String tid);
}
