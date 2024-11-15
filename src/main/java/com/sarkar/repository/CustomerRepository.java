package com.sarkar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sarkar.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
	public Customer findByEmail(String email);

}
