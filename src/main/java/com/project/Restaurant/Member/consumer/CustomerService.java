package com.project.Restaurant.Member.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final PasswordEncoder passwordEncoder;
  public void create(String username, String password, String nickname, String email) {
    Customer customer = new Customer();
    customer.setUsername(username);
    customer.setPassword(passwordEncoder.encode(password));
    customer.setNickname(nickname);
    customer.setEmail(email);
    customer.setSignupDate(LocalDateTime.now());
    customer.setMemberActivation(true);
    customerRepository.save(customer);
  }

  public Customer findByusername(String username) {
    if (customerRepository.findByusername(username).isPresent()) {
      return customerRepository.findByusername(username).get();
    } else {
      return null;
    }
  }

  public Customer findByEmail(String email) {
    if (customerRepository.findByemail(email).isPresent()) {
      return customerRepository.findByemail(email).get();
    } else {
      return null;
    }
  }
}
