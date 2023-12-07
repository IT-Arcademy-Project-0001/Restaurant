package com.project.Restaurant.Member.owner;

import com.project.Restaurant.Member.MemberRole;
import com.project.Restaurant.Member.consumer.Customer;
import com.project.Restaurant.Member.consumer.CustomerDetails;
import com.project.Restaurant.Member.consumer.CustomerRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
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
public class OwnerOauth2UserService extends DefaultOAuth2UserService {

    private final OwnerRepository ownerRepository;
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
        System.out.println("owner");
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider + "_" +providerId;

        Optional<Owner> _owner = ownerRepository.findByusername(username);

        Owner owner = new Owner();
        if(_owner.isEmpty()) {
            owner.setUsername(username);
            owner.setProvider(provider);
            owner.setProviderId(providerId);
            owner.setNickname(oAuth2User.getAttribute("name"));
            owner.setEmail(oAuth2User.getAttribute("email"));
            owner.setAuthority(MemberRole.OWNER.getValue());
            owner.setSignupDate(LocalDateTime.now());
            owner.setCode(getRandomCode());
            ownerRepository.save(owner);
        } else {
            owner = _owner.get();
        }

        return new OwnerDetails(owner, oAuth2User.getAttributes());
    }
}
