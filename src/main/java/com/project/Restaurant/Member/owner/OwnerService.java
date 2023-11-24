package com.project.Restaurant.Member.owner;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class OwnerService {

  private final OwnerRepository ownerRepository;
  private final PasswordEncoder passwordEncoder;
  public void create(String username, String password, String nickname, String email) {
    Owner owner = new Owner();
    owner.setUsername(username);
    owner.setPassword(passwordEncoder.encode(password));
    owner.setNickname(nickname);
    owner.setEmail(email);
    owner.setSignupDate(LocalDateTime.now());
    owner.setMemberActivation(true);
    ownerRepository.save(owner);
  }

  public Owner getUser(String username) {
    return ownerRepository.findByusername(username).get();
  }

  public Owner findByEmail(String email) {
    if (ownerRepository.findByemail(email).isPresent()) {
      return ownerRepository.findByemail(email).get();
    } else {
      return null;
    }
  }
}
