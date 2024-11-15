package com.sarkar.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sarkar.entity.Customer;
import com.sarkar.repository.CustomerRepository;
import com.sarkar.service.JWTService;

@RestController
public class CoustomerRestController {
	@Autowired
	private CustomerRepository customerRepo;

	@Autowired
	private PasswordEncoder pwdEncoder;

	@Autowired
	private AuthenticationManager authmanager;
	
	@Autowired
	private JWTService jwtservice;
	
	@GetMapping("/welcome")
	public String welcome() {
		return "hello prahlad";
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> loginCheck(@RequestBody Customer customer) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(customer.getEmail(),
				customer.getPwd());
		try {
			Authentication authenticate = authmanager.authenticate(token);
			if (authenticate.isAuthenticated()) {
				
				String jwt = jwtservice.generateToken(customer.getEmail());
				return new ResponseEntity<>(jwt, HttpStatus.OK);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}                         
		return new ResponseEntity<>("Invalid Credentials", HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/register")

	public ResponseEntity<String> saveCustomer(@RequestBody Customer customer) {

		String encodedPwd = pwdEncoder.encode(customer.getPwd());
		customer.setPwd(encodedPwd);

		customerRepo.save(customer);

		return new ResponseEntity<>("Customer Registered", HttpStatus.CREATED);

	}

}
