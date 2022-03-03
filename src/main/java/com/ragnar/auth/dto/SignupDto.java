package com.ragnar.auth.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class SignupDto {

	@NotBlank(message = "firstName is mandatory")
	private String name;
	
	@NotBlank(message = "firstName is mandatory")
	private String username;
	
	@NotBlank(message = "firstName is mandatory")
	private String email;
	
	@NotBlank(message = "firstName is mandatory")
	private String password;
}