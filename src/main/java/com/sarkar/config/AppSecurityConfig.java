package com.sarkar.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sarkar.service.CustomerService;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

	@Autowired
	private CustomerService customerservice;
	
	@Autowired
	private AppFilter appfilter;

	@Bean
	public PasswordEncoder pwdEncoder(){
		return new BCryptPasswordEncoder();
		

	}

	@Bean
	public AuthenticationProvider authProvider() {

		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(customerservice);
		authProvider.setPasswordEncoder(pwdEncoder());
		return authProvider;

	}

	@Bean
	public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {

		return config.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain SecurityCcinfig(HttpSecurity http) throws Exception {
		http.csrf().disable();
		
//		http.authorizeHttpRequests(req -> {
//			req.requestMatchers("/register", "/login")
//			.permitAll()
//			.anyRequest()
//			.authenticated();
//		});
//		return http.build();
		
	return	http.authorizeHttpRequests()
		.requestMatchers("/login","/register")
		.permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authenticationProvider(authProvider())
		.addFilterBefore(appfilter, UsernamePasswordAuthenticationFilter.class)
		.build();

	}
}
