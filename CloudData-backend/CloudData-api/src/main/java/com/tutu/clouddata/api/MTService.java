package com.tutu.clouddata.api;

import java.util.List;

import com.tutu.clouddata.dto.View;
import com.tutu.clouddata.model.MFJsonViews;
import com.tutu.clouddata.model.MT;

public interface MTService {
	List<MT> list();

	MT mt(String mid);

	void save(MT mt);

	void addMf(MFJsonViews mf, String mtid);

	void addView(View view, String mtid);
}
