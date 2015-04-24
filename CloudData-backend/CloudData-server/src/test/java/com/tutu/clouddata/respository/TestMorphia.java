package com.tutu.clouddata.respository;

import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.Mongo;
import com.tutu.clouddata.api.UserService;
import com.tutu.clouddata.auth.dao.SystemDatastore;
import com.tutu.clouddata.basicTest.BaseServiceTests;
import com.tutu.clouddata.context.Context;
import com.tutu.clouddata.context.ContextHolder;
import com.tutu.clouddata.dto.Org;
import com.tutu.clouddata.dto.auth.MM;
import com.tutu.clouddata.dto.auth.Tenant;
import com.tutu.clouddata.dto.auth.User;
import com.tutu.clouddata.model.MF;
import com.tutu.clouddata.model.MFText;
import com.tutu.clouddata.model.MT;
import com.tutu.clouddata.session.PwdUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:conf/spring.xml")
public class TestMorphia extends BaseServiceTests {
	public UserService userService;
	private SystemDatastore systemDatastore;
	@Before
	public void Login() throws UnknownHostException{
		userService=context.getBean(UserService.class);
		systemDatastore=context.getBean(SystemDatastore.class);
		String username = "admin@test.com";
		String password = "000000";
		try {
			userService.authenticate(username, password);
		} catch (Exception e) {
			return;
		}
		Context context = new Context();
		User user = systemDatastore.get(User.class, username);
		context.setUser(user);
		ContextHolder.setContext(context);
	}
	
	@Test
	public void RegistTenant() throws UnknownHostException, NoSuchAlgorithmException {
		userService=context.getBean(UserService.class);
		systemDatastore=context.getBean(SystemDatastore.class);
		MM mm = new MM();
		mm.setId(new ObjectId());
		mm.setHostip("127.0.0.1");
		mm.setPort(27017);
		mm.setWeight(1);
		systemDatastore.save(mm);

		Tenant tenant = new Tenant();
		tenant.setName("娴嬭瘯鏈烘瀯");
		tenant.setMm(mm);
		tenant.setDbname("somedatabase");
		systemDatastore.save(tenant);
		User user = new User("admin@test.com", PwdUtils.eccrypt("000000"));
		user.setTenant(tenant);
		systemDatastore.save(user);
	}

	@Test
	public void LoginAndCreateMT() throws UnknownHostException {
		userService=context.getBean(UserService.class);
		systemDatastore=context.getBean(SystemDatastore.class);
		String username = "admin@test.com";
		String password = "000000";
		try {
			userService.authenticate(username, password);
		} catch (Exception e) {
			return;
		}
		Context context = new Context();
		User user = systemDatastore.get(User.class, username);
		context.setUser(user);
		ContextHolder.setContext(context);

		MT mt = new MT();
		mt.setName("测试表");
		MF mf = new MFText();
		mf.setKey("测试字段");
		List<MF> mfs = new ArrayList<MF>();
		mfs.add(mf);
		mt.setMfs(mfs);
		ContextHolder.getContext().getDatastore().save(mt);
		MT mymt=ContextHolder.getContext().getDatastore().get(MT.class,"5477db7611d504bf46c88862");
	}
	@Test
	public void createOrg() throws UnknownHostException{
		Mongo mongo=new Mongo("127.0.0.1",27017);
		Datastore ds=new Morphia().createDatastore(mongo, "somedatabase");
//		Org parentOrg=ds.get(Org.class,"547b33577d8dc8ba351295e3");
//		Org org=new Org();
//		org.setName("测试机构3");
//		org.setParentOrg(parentOrg);
		List<Org> orgs=ds.find(Org.class).asList();
		orgs.get(0);
	}
	
	

}
