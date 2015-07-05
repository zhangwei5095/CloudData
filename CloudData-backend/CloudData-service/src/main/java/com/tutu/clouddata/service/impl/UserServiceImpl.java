package com.tutu.clouddata.service.impl;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tutu.clouddata.api.SequenceService;
import com.tutu.clouddata.api.UserService;
import com.tutu.clouddata.auth.dao.SystemDatastore;
import com.tutu.clouddata.context.ContextHolder;
import com.tutu.clouddata.dto.TokenTransfer;
import com.tutu.clouddata.dto.UserTransfer;
import com.tutu.clouddata.dto.auth.AccessToken;
import com.tutu.clouddata.dto.auth.MM;
import com.tutu.clouddata.dto.auth.Tenant;
import com.tutu.clouddata.dto.auth.User;
import com.tutu.clouddata.session.PwdUtils;
import com.tutu.clouddata.session.TokenUtils;

@Service("userService")
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserServiceImpl implements UserService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private SystemDatastore systemDatastore;
	@Resource
	private SequenceService sequenceService;

	/**
	 * Retrieves the currently logged in user.
	 * 
	 * @return A transfer containing the username and the roles.
	 */
	@GET
	public UserTransfer getUser() {
		User user = ContextHolder.getContext().getUser();
		if (user == null)
			return null;
		return new UserTransfer(user.getName(), createRoleMap(user));
	}

	/**
	 * Authenticates a user and creates an authentication token.
	 * 
	 * @param username
	 *            The name of the user.
	 * @param password
	 *            The password of the user.
	 * @return A transfer containing the authentication token.
	 */
	@Path("authenticate")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public TokenTransfer authenticate(@FormParam("username") String username,
			@FormParam("password") String password) {
		User user = systemDatastore.get(User.class, username);
		String token = null;
		try {
			if (user == null
					|| !user.getPassword().equals(PwdUtils.eccrypt(password))) {
				throw new WebApplicationException(401);
			} else {
				token = TokenUtils.createToken(user);
				AccessToken accessToken = new AccessToken();
				accessToken.setToken(token);
				accessToken.setTokenDate(new Date());
				accessToken.setUserId(user.getName());
				systemDatastore.save(accessToken);
			}
		} catch (NoSuchAlgorithmException e) {
			logger.error("some thing wrong", e);
		}
		TokenTransfer tokenTransfer = new TokenTransfer(token);
		tokenTransfer.setRoles(user.getRoles().toArray(
				new String[user.getRoles().size()]));
		tokenTransfer.setName(user.getName());
		return tokenTransfer;
	}

	@Path("signup")
	@POST
	public void signUp(@FormParam("orgname") String orgname,
			@FormParam("username") String username,
			@FormParam("password") String password) {
		Tenant tenant = systemDatastore.get(Tenant.class, orgname);
		User user = systemDatastore.get(User.class, username);
		if (tenant != null || user != null)
			throw new WebApplicationException(401);
		tenant = new Tenant();
		tenant.setName(orgname);
		List<MM> mms = systemDatastore.find(MM.class).asList();
		tenant.setMm(getMMFromWeight(mms));
		Long tenantId = sequenceService.getNextId("tenantId", 0L);
		tenant.setDbname("t_" + tenantId);
		user = new User();
		user.setName(username);
		try {
			user.setPassword(PwdUtils.eccrypt(password));
		} catch (NoSuchAlgorithmException e) {
			logger.error("加密出错!", e);
		}
		user.setTenant(tenant);
		user.setOrgId("1");
		Set<String> roles = new HashSet<String>();
		roles.add("admin");
		user.setRoles(roles);
		systemDatastore.save(tenant);
		systemDatastore.save(user);
	}

	private MM getMMFromWeight(List<MM> mms) {
		int totalweight = 0;
		for (MM mm : mms) {
			totalweight += mm.getWeight();
		}
		int randomIndex = -1;
		double random = Math.random() * totalweight;
		for (int i = 0; i < mms.size(); ++i) {
			random -= mms.get(i).getWeight();
			if (random <= 0.0d) {
				randomIndex = i;
				break;
			}
		}
		return mms.get(randomIndex);
	}

	private Map<String, Boolean> createRoleMap(User user) {
		Map<String, Boolean> roles = new HashMap<String, Boolean>();
		// for (String role : user.getRoles()) {
		// roles.put(role, Boolean.TRUE);
		// }
		return roles;
	}

	@Path("filterByOrg/{orgId}")
	@GET
	public List<User> getUsersByOrg(@PathParam("orgId") String orgId) {
		return systemDatastore.find(User.class, "orgId", orgId).asList();
	}

	@Path("all")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> all() {
		return systemDatastore.find(User.class).asList();
	}

	@POST
	public void save(User user) {
		try {
			user.setPassword(PwdUtils.eccrypt(user.getPassword()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		systemDatastore.save(user);
	}

	@Path("checkOrgName/{orgName}")
	@GET
	public boolean checkOrgName(@PathParam("orgName") String orgName) {
		Tenant tenant = systemDatastore.find(Tenant.class)
				.filter("name", orgName).get();
		return tenant != null;
	}

}
