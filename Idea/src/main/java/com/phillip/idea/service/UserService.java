package com.phillip.idea.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.phillip.idea.domain.User;

public interface UserService extends UserDetailsService{
	User findOne(String uuid);
	User findUserByEmail(String username);
	User getUserFromSession();
	void setUserInSession(User user);
}
