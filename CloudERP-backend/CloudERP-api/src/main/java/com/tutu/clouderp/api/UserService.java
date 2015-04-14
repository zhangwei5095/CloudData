package com.tutu.clouderp.api;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tutu.clouderp.dto.TokenTransfer;
import com.tutu.clouderp.dto.UserTransfer;
import com.tutu.clouderp.dto.auth.User;
@Service
public interface UserService {
	public UserTransfer getUser();
	public List<User> getUsersByOrg(String orgId);
	public List<User> all();
	public TokenTransfer authenticate(String username, String password);
}
