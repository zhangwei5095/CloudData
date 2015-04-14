package com.tutu.clouderp.respository;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.Mongo;
import com.tutu.clouderp.dto.Org;
import com.tutu.clouderp.dto.Role;
import com.tutu.clouderp.dto.auth.User;
import com.tutu.clouderp.model.MF;
import com.tutu.clouderp.model.MFCheckBox;
import com.tutu.clouderp.model.MFDate;
import com.tutu.clouderp.model.MFNumber;
import com.tutu.clouderp.model.MFSelect;
import com.tutu.clouderp.model.MFText;
import com.tutu.clouderp.model.MT;

public class TestWithoutAuth {
	public Datastore ds;
	@Before
	public void initDs() throws UnknownHostException{
		Mongo mongo=new Mongo("10.255.242.25",27017);
		ds=new Morphia().createDatastore(mongo, "sysmongo");
	}
	

	@Test
	public void createMT() throws UnknownHostException{
		MT mt=new MT();
		mt.setName("客户");
		List<MF> mfs=new ArrayList<MF>();
		
		MFText mfText=new MFText();
		mfText.setLabel("名称");
		mfText.setKey("name");
		mfText.setIsunique(true);
		mfText.setPlaceholder("张三...");
		mfText.setDescription("名称");
		mfText.setRequired(true);
		
		MFDate mfDate=new MFDate();
		mfDate.setKey("birthday");
		mfDate.setLabel("生日");
		mfDate.setFormat("yyyy-MM-dd");

		MFSelect mfSelect=new MFSelect();
		mfSelect.setKey("gender");
		mfSelect.setLabel("性别");
		mfSelect.setOptions(new String[]{"男","女"});
		
		MFNumber mfNumber=new MFNumber();
		mfNumber.setKey("age");
		mfNumber.setLabel("年龄");
		
		MFCheckBox mfCheckBox=new MFCheckBox();
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
	public void createOrg(){
		Org org=new Org();
		org.setId("1");
		org.setName("总公司");
		
		List<Org> children=new ArrayList<Org>();
		Org org1=new Org();
		org1.setName("分公司");
		org1.setId("2");
		children.add(org1);
		
		org.setChildren(children);
		
		ds.save(org);
	}
	@Test
	public void createUser(){
		User user=new User();
		user.setName("测试用户");
		user.setOrgId("1");
		ds.save(user);
	}
	
	@Test
	public void createRole(){
		Role role=new Role();
		role.setName("测试用户");
		role.setId("1");
		
		Role role2=new Role();
		role2.setName("管理员");
		role2.setId("2");
		
		Role role3=new Role();
		role3.setName("总经理");
		role3.setId("3");
		ds.save(role);
		ds.save(role2);
		ds.save(role3);
	}
	
}
