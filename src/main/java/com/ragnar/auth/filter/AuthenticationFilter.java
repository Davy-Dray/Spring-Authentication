package com.ragnar.auth.filter;

import java.io.IOException;
import java.security.SignatureException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.StringUtils;

import com.ragnar.auth.service.UserService;
import com.ragnar.auth.util.TokenProvider;

public class AuthenticationFilter extends OncePerRequestFilter {

	// inject dependencies
	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
	private UserService UserService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// get JWT (token) from http request
		String token = getJWTfromRequest(request);
		// validate token
		try {
			if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
				// get username from token
				String username = tokenProvider.getUsernameFromJWT(token);
				// load user associated with token
				UserDetails userDetails = UserService.loadUserByUsername(username);

				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// set spring security
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		} catch (UsernameNotFoundException | SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		filterChain.doFilter(request, response);
	}

	// Bearer <accessToken>
	private String getJWTfromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}

}
