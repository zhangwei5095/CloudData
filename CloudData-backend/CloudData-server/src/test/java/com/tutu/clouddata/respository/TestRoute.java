package com.tutu.clouddata.respository;
//package com.tutu.clouddata.respository;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.bson.types.ObjectId;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.tutu.clouddata.Entity.MF;
//import com.tutu.clouddata.Entity.MFText;
//import com.tutu.clouddata.Entity.MT;
//import com.tutu.clouddata.MultiTenant.MultiTenantMongoDbFactory;
//import com.tutu.clouddata.auth.entity.MM;
//import com.tutu.clouddata.auth.entity.Org;
//import com.tutu.clouddata.auth.entity.User;
//import com.tutu.clouddata.auth.repository.MMRepository;
//import com.tutu.clouddata.auth.repository.OrgRepository;
//import com.tutu.clouddata.auth.repository.UserRepository;
//import com.tutu.clouddata.context.Context;
//import com.tutu.clouddata.context.ContextHolder;
//import com.tutu.clouddata.repository.MFRepository;
//import com.tutu.clouddata.repository.MTRepository;
//import com.tutu.clouddata.rest.resources.UserResource;
//import com.tutu.clouddata.session.Hex;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath:context.xml")
//public class TestRoute extends AbstractJUnit4SpringContextTests {
//	@Resource
//    private OrgRepository orgRepository;
//	@Resource
//    private UserRepository userRespository;
//	@Resource
//	private UserResource userResource;
//	@Resource
//	private MMRepository mmRepository;
//	@Resource
//	private MTRepository mtRepository;
//	@Resource
//	private MFRepository mfRepository;
//	@Test
//	public void RegistOrg(){
//		Org org=new Org();
//		ObjectId objectId=new ObjectId();
//		org.setId(objectId.toString());
//		org.setName("测试机构");
//		org.setMmid("5471b3cdc2dfebe7e627b828");
//		org.setDbid("somedatabase");
//		org=orgRepository.save(org);
//		User user=new User("admin@test.com",new String(Hex.encode("000000".getBytes())));
//		user.setMoid(org.getId());
//		userRespository.save(user);
//	}
//	
//	@Test
//	public void LoginAndCreateMT(){
//		String username="admin@test.com";
//		String password="000000";
//		try{
//			userResource.authenticate(username, password);
//		}catch(Exception e){
//			return;
//		}
//		Context context=new Context();
//		User user=userRespository.findByName(username);
//		context.setUser(user);
//		Org org=orgRepository.findOne(user.getMoid());
//		context.setOrg(org);
//		MM mm=mmRepository.findOne(org.getMmid());
//		context.setMm(mm);
//		ContextHolder.setContext(context);
//		
////		try {
////			Mongo mongo=new Mongo(mm.getHostip(),mm.getPort());
////			MultiTenantMongoDbFactory multiTenantMongoDbFactory=new MultiTenantMongoDbFactory(mongo, org.getDbid());
////			multiTenantMongoDbFactory.setMongoTemplate(multiTenantMongoTemplate);
//			MultiTenantMongoDbFactory.setDatabaseNameForCurrentThread(org.getDbid());
//			MT mt=new MT();
//			mt.setOid(org.getId());
//			mt.setName("测试表");
//			MF mf=new MFText();
//			mf.setName("测试字段"); 
//			List<MF> mfs=new ArrayList<MF>();
//			mfs.add(mf);
//			mt=mtRepository.save(mt);
//			
//			
//			mfRepository.save(mf);
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//}
