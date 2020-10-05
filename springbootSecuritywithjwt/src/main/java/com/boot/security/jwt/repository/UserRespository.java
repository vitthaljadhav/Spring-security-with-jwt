package com.boot.security.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boot.security.jwt.model.User;

@Repository
public interface UserRespository extends JpaRepository<User, Integer> {

	Optional<User> findByUsername(String username);
	
}
