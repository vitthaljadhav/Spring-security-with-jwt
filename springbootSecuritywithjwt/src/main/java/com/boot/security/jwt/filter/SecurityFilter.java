package com.boot.security.jwt.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.boot.security.jwt.utilitiy.JwtUtil;

@Component
public class SecurityFilter extends OncePerRequestFilter{

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//1. read token from Auth header
		
		String token = request.getHeader("Authorization");
		if(token!=null) {
			//do validation
			
			String username = jwtUtil.getUsername(token);
			// username should not be empty , context -auth must be empty
			if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
				UserDetails user = userDetailsService.loadUserByUsername(username);
				
				//validate token
				
				boolean isValid = jwtUtil.validateToken(token, user.getUsername());
				
				if(isValid) {
					UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(username, user.getPassword(),user.getAuthorities());
					
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request) );
					// final Object stored in  SecurityContext with User Details (username, password)
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					
				}
				
			}
			
			
			
		}
		
		filterChain.doFilter(request, response);
		
	}

}
