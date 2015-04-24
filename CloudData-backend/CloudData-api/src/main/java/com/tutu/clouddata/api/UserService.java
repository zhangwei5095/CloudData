package com.tutu.clouddata.api;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tutu.clouddata.dto.TokenTransfer;
import com.tutu.clouddata.dto.UserTransfer;
import com.tutu.clouddata.dto.auth.User;
@Service
public interface UserService {
	public UserTransfer getUser();
	public List<User> getUsersByOrg(String orgId);
	public List<User> all();
	public TokenTransfer authenticate(String username, String password);
}
