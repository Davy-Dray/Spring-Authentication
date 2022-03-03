package com.ragnar.auth.service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ragnar.auth.model.User;
import com.ragnar.auth.repository.UserRepository;

import lombok.Setter;

@Service
public class UserService implements UserDetailsService {

	// inject the user repository
	@Autowired
	private UserRepository userRepository;


	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	// load the username and password
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User user = userRepository.findByEmail(email);

		if (user == null) {

			throw new UsernameNotFoundException("user not found " + email);
		} else {
			return user;

		}

	}

}
