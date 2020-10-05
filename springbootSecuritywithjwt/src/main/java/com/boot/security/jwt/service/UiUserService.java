package com.boot.security.jwt.service;

import java.util.Optional;

import com.boot.security.jwt.model.User;

public interface UiUserService {

	public Integer saveUser(User user);
	
	public Optional<User> findByUsername(String username);
	
}
