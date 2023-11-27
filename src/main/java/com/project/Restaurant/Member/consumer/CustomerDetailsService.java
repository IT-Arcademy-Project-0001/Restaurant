package com.project.Restaurant.Member.consumer;

import com.project.Restaurant.Member.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomerDetailsService implements UserDetailsService {

  private final CustomerRepository customerRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    System.out.println("유저");
    Optional<Customer> _customer = customerRepository.findByusername(username);
    if (_customer.isEmpty()) {
      throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
    }
    Customer customer = _customer.get();
    List<GrantedAuthority> authorities = new ArrayList<>();
    if ("admin".equals(username)) {
      authorities.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getValue()));
    } else {
      authorities.add(new SimpleGrantedAuthority(MemberRole.CUSTOMER.getValue()));
    }
    return new User(customer.getUsername(), customer.getPassword(), authorities);
  }

}
