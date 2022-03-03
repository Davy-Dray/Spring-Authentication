package com.ragnar.auth.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ragnar.auth.dto.JWTAuthResponse;
import com.ragnar.auth.dto.LoginDto;
import com.ragnar.auth.dto.SignupDto;
import com.ragnar.auth.enums.AppUserRole;
import com.ragnar.auth.exception.EmailTaken;
import com.ragnar.auth.exception.ExceptionHandler;
import com.ragnar.auth.exception.UsernameTaken;
import com.ragnar.auth.model.User;
import com.ragnar.auth.repository.UserRepository;
import com.ragnar.auth.util.TokenProvider;

@RestController
@RequestMapping({ "/", "/user" })
public class AuthController extends ExceptionHandler {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/signin")
	public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto) {
		//authenticate the user
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		// get token form tokenProvider
		String token = tokenProvider.generateToken(authentication);

		return ResponseEntity.ok(new JWTAuthResponse(token));
	}

	@PostMapping("/signup")
	public void registerUser(@Valid @RequestBody SignupDto signUpDto) throws UsernameTaken, EmailTaken {

		//  check for username exists in a DB
		if (userRepository.existsByUsername(signUpDto.getUsername())) {
			
			throw new UsernameTaken("username taken");
		}

		//  check for email exists in DB
		if (userRepository.existsByEmail(signUpDto.getEmail())) {
			throw new EmailTaken("email taken");
		}

		// create user object
		User user = new User();
		user.setName(signUpDto.getName());
		user.setUsername(signUpDto.getUsername());
		user.setEmail(signUpDto.getEmail());
		user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
		user.setRoles(AppUserRole.USER);

		userRepository.save(user);


	}

	@GetMapping("/users")
	public List<User> getUsers() {

		return userRepository.findAll();
	}
}
