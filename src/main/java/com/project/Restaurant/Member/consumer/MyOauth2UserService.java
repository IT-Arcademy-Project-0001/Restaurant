package com.project.Restaurant.Member.consumer;

import com.project.Restaurant.Member.MemberRole;
import com.project.Restaurant.Member.owner.OwnerOauth2UserService;
import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyOauth2UserService extends DefaultOAuth2UserService {

    private final OwnerOauth2UserService ownerOauth2UserService;
    private final CustomerRepository customerRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = req.getSession();

        String uri = (String)session.getAttribute("requestUri");

        if(uri.startsWith("/owner")) {
            return ownerOauth2UserService.loadUser(userRequest);
        }

        System.out.println("customer");

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
            customer.setAuthority(MemberRole.CUSTOMER.getValue());
            customer.setSignupDate(LocalDateTime.now());
            customerRepository.save(customer);
        } else {
            customer = _customer.get();
        }

        return new CustomerDetails(customer, oAuth2User.getAttributes());
    }

}
