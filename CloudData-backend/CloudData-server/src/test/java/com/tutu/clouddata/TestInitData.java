package com.tutu.clouddata;

import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.Mongo;
import com.tutu.clouddata.dto.Org;
import com.tutu.clouddata.dto.Role;
import com.tutu.clouddata.dto.auth.MM;
import com.tutu.clouddata.dto.auth.Tenant;
import com.tutu.clouddata.dto.auth.User;
import com.tutu.clouddata.model.MF;
import com.tutu.clouddata.model.MFCheckBox;
import com.tutu.clouddata.model.MFDate;
import com.tutu.clouddata.model.MFNumber;
import com.tutu.clouddata.model.MFSelect;
import com.tutu.clouddata.model.MFText;
import com.tutu.clouddata.model.MT;
import com.tutu.clouddata.session.PwdUtils;

public class TestInitData {
	public Datastore sysDs;
	public Datastore ds;
	public Mongo mongo;
	@Before
	public void initDs() throws UnknownHostException {
		mongo = new Mongo("localhost", 27017);
		sysDs = new Morphia().createDatastore(mongo, "sysmongo");
		ds = new Morphia().createDatastore(mongo, "c_1");
	}

	@Test
	public void createMT() throws UnknownHostException {
		MT mt = new MT();
		mt.setName("客户");
		List<MF> mfs = new ArrayList<MF>();

		MFText mfText = new MFText();
		mfText.setLabel("名称");
		mfText.setKey("name");
		mfText.setIsunique(true);
		mfText.setPlaceholder("张三...");
		mfText.setDescription("名称");
		mfText.setRequired(true);

		MFDate mfDate = new MFDate();
		mfDate.setKey("birthday");
		mfDate.setLabel("生日");
		mfDate.setFormat("yyyy-MM-dd");

		MFSelect mfSelect = new MFSelect();
		mfSelect.setKey("gender");
		mfSelect.setLabel("性别");
		mfSelect.setOptions(new String[] { "男", "女" });

		MFNumber mfNumber = new MFNumber();
		mfNumber.setKey("age");
		mfNumber.setLabel("年龄");

		MFCheckBox mfCheckBox = new MFCheckBox();
		mfCheckBox.setLabel("是否结婚");
		mfCheckBox.setKey("isMarried");

		mfs.add(mfText);
		mfs.add(mfDate);
		mfs.add(mfSelect);
		mfs.add(mfNumber);
		mfs.add(mfCheckBox);

		mt.setMfs(mfs);
		ds.save(mt);
	}

	@Test
	public void createOrg() {
		Org org = new Org();
		org.setId("1");
		org.setName("总公司");

		List<String> children = new ArrayList<String>();
		Org org1 = new Org();
		org1.setName("分公司");
		org1.setId("2");
		org1.setPid("1");
		org1.setParentIds("1");
		children.add("2");

		Org org2 = new Org();
		org2.setName("子公司");
		org2.setId("3");
		org2.setPid("2");
		org2.setParentIds("1,2");

		ds.save(org);
		ds.save(org1);
		ds.save(org2);
	}

	@Test
	public void createRole() {
		Role role = new Role("1");
		role.setName("测试用户");

		Role role2 = new Role("2");
		role2.setName("管理员");

		Role role3 = new Role("3");
		role3.setName("总经理");
		ds.save(role);
		ds.save(role2);
		ds.save(role3);
	}
	
	@Test
	public void createMMAndTenant() throws NoSuchAlgorithmException {
		MM mm=new MM();
		mm.setHostip("localhost");
		mm.setPort(27017);
		sysDs.save(mm);
		Tenant tenant=new Tenant();
		tenant.setMm(mm);
		tenant.setName("测试公司");
		tenant.setDbname("c_1");
		sysDs.save(tenant);
		User user = new User();
		user.setName("admin");
		user.setPassword(PwdUtils.eccrypt("000000"));
		user.setTenant(tenant);
		user.setOrgId("1");
		sysDs.save(user);
		user.setTenant(null);
		ds.save(user);
	}
	
}
