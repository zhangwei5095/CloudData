package com.tutu.clouddata.service.impl;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.tutu.clouddata.api.UserService;
import com.tutu.clouddata.auth.dao.SystemDatastore;
import com.tutu.clouddata.context.ContextHolder;
import com.tutu.clouddata.dto.TokenTransfer;
import com.tutu.clouddata.dto.UserTransfer;
import com.tutu.clouddata.dto.auth.AccessToken;
import com.tutu.clouddata.dto.auth.User;
import com.tutu.clouddata.session.PwdUtils;
import com.tutu.clouddata.session.TokenUtils;

@Service("userService")
@Path("/user")
public class UserServiceImpl implements UserService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private SystemDatastore systemDatastore;

	/**
	 * Retrieves the currently logged in user.
	 * 
	 * @return A transfer containing the username and the roles.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
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
	public TokenTransfer authenticate(@FormParam("username") String username, @FormParam("password") String password) {
		User user = systemDatastore.get(User.class, username);
		String token = null;
		try {
			if (user == null || !user.getPassword().equals(PwdUtils.eccrypt(password))) {
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
		TokenTransfer tokenTransfer=new TokenTransfer(token);
		tokenTransfer.setRoles(user.getRoles().toArray(new String[user.getRoles().size()]));
		tokenTransfer.setName(user.getName());
		return tokenTransfer;
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
	@Produces(MediaType.APPLICATION_JSON)
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
	@Consumes(MediaType.APPLICATION_JSON)
	public void save(User user) {
		try {
			user.setPassword(PwdUtils.eccrypt(user.getPassword()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		systemDatastore.save(user);
	}
}
