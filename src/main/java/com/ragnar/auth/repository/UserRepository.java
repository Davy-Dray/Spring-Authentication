package com.ragnar.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ragnar.auth.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

	User findByEmail(String email);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
}
