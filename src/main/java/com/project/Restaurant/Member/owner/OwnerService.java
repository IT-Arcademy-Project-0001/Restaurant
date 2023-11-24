package com.project.Restaurant.Member.owner;

import com.project.Restaurant.Member.consumer.Customer;
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
        owner.setAuthority("OWNER");
        ownerRepository.save(owner);
    }

    public void resetPassword(Owner owner, String password) {
        owner.setPassword(passwordEncoder.encode(password));
        ownerRepository.save(owner);
    }

    public Owner findByusername(String username) {
        if (ownerRepository.findByusername(username).isPresent()) {
            return ownerRepository.findByusername(username).get();
        } else {
            return null;
        }
    }

    public Owner findByEmail(String email) {
        if (ownerRepository.findByemail(email).isPresent()) {
            return ownerRepository.findByemail(email).get();
        } else {
            return null;
        }
    }
}
