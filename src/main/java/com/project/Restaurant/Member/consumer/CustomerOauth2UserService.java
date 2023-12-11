package com.project.Restaurant.Member.consumer;

import com.project.Restaurant.Member.MemberRole;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerOauth2UserService extends DefaultOAuth2UserService {

    private final CustomerRepository customerRepository;
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

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider + "_" +providerId;

        Optional<Customer> _customer = customerRepository.findByusername(username);

        Customer customer = new Customer();
        if(_customer.isEmpty()) {
            customer.setUsername(username);
            customer.setProvider(provider);
            customer.setProviderId(providerId);
            customer.setNickname(oAuth2User.getAttribute("name"));
            customer.setEmail(oAuth2User.getAttribute("email"));
            customer.setAuthority(MemberRole.CUSTOMER.getValue());
            customer.setSignupDate(LocalDateTime.now());
            customer.setCode(getRandomCode());
            customer.setPhoto(oAuth2User.getAttribute("picture"));
            customerRepository.save(customer);
        } else {
            customer = _customer.get();
        }

        return new CustomerDetails(customer, oAuth2User.getAttributes());
    }

}
