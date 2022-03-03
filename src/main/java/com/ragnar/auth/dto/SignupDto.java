package com.ragnar.auth.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class SignupDto {

	@NotBlank(message = "Name is mandatory")
	private String name;
	
	@NotBlank(message = "username is mandatory")
	private String username;
	
	@NotBlank(message = "email is mandatory")
	private String email;
	
	@NotBlank(message = "password is mandatory")
	private String password;
}