package com.boot.security.jwt.service.impl;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.boot.security.jwt.model.User;
import com.boot.security.jwt.repository.UserRespository;
import com.boot.security.jwt.service.UiUserService;

@Service
public class UiUserServiceImpl implements UiUserService, UserDetailsService {
	@Autowired
	private UserRespository userRepo;// HAS-A

	@Autowired
	private BCryptPasswordEncoder pwdEncoder;

	// save Method
	@Override
	public Integer saveUser(User user) {

		// Encode Password
		user.setPassword(pwdEncoder.encode(user.getPassword()));
		User save = userRepo.save(user);
		return user.getId();
	}

	// get user by Username

	@Override
	public Optional<User> findByUsername(String username) {
		return userRepo.findByUsername(username);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<User> opt = findByUsername(username);
		if (!opt.isPresent())
			throw new UsernameNotFoundException("user Not Exist");

		// read user from DataBase
		User user = opt.get();

		return new org.springframework.security.core.userdetails.User(username, user.getPassword(),
				user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList())

		);
	}

}
