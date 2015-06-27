package com.tutu.clouddata.api;

import java.util.List;

import com.tutu.clouddata.dto.TokenTransfer;
import com.tutu.clouddata.dto.UserTransfer;
import com.tutu.clouddata.dto.auth.User;

public interface UserService {
	public UserTransfer getUser();

	public List<User> getUsersByOrg(String orgId);

	public List<User> all();

	public TokenTransfer authenticate(String username, String password);
	
	void signUp(String orgName,String userName,String password);

	void save(User user);
}
