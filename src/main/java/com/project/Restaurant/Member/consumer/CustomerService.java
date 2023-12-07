package com.project.Restaurant.Member.consumer;

import com.project.Restaurant.Member.MemberRole;
import com.project.Restaurant.Member.owner.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
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
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setPassword(passwordEncoder.encode(password));
        customer.setNickname(nickname);
        customer.setEmail(email);
        customer.setSignupDate(LocalDateTime.now());
        customer.setMemberActivation(false);
        customer.setAuthority(MemberRole.CUSTOMER.getValue());
        customer.setCode(getRandomCode());
        customer.setPhoto("/member/profilePhoto/human.png");
        customerRepository.save(customer);
    }

    public void resetPassword(Customer customer, String password) {
        customer.setPassword(passwordEncoder.encode(password));
        String newCode = getRandomCode();
        customer.setCode(newCode);
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

    public void customerActivation(Customer customer, boolean activation) {
        customer.setMemberActivation(activation);
        String newCode = getRandomCode();
        customer.setCode(newCode);
        customerRepository.save(customer);
    }

    public void deleteCustomer(Customer customer) {
        customerRepository.delete(customer);
    }
}
