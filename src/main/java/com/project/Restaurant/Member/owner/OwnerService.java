package com.project.Restaurant.Member.owner;

import com.project.Restaurant.Member.MemberRole;
import com.project.Restaurant.Member.consumer.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;
    private final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // 사용할 문자셋

    public String getRandomCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(6); // 6자리의 코드 생성
        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public void create(String username, String password, String nickname, String email) {
        Owner owner = new Owner();
        owner.setUsername(username);
        owner.setPassword(passwordEncoder.encode(password));
        owner.setNickname(nickname);
        owner.setEmail(email);
        owner.setSignupDate(LocalDateTime.now());
        owner.setMemberActivation(false);
        owner.setAuthority(MemberRole.OWNER.getValue());
        owner.setCode(getRandomCode());
        owner.setPhoto("/member/profilePhoto/human.png");
        ownerRepository.save(owner);
    }

    public void resetPassword(Owner owner, String password) {
        owner.setPassword(passwordEncoder.encode(password));
        String newCode = getRandomCode();
        owner.setCode(newCode);
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

    public void ownerActivation(Owner owner, boolean activation) {
        owner.setMemberActivation(activation);
        String newCode = getRandomCode();
        owner.setCode(newCode);
        ownerRepository.save(owner);
    }

    public void deleteOwner(Owner owner) {
        ownerRepository.delete(owner);
    }
}
