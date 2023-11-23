package com.project.Restaurant.Member.consumer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
  Optional<Customer> findByusername(String username);
  Optional<Customer> findByemail(String email);
}
