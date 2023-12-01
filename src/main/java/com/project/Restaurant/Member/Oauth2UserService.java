package com.project.Restaurant.Member;

import com.project.Restaurant.Member.consumer.Customer;
import com.project.Restaurant.Member.consumer.CustomerDetails;
import com.project.Restaurant.Member.consumer.CustomerOauth2UserService;
import com.project.Restaurant.Member.consumer.CustomerRepository;
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
public class Oauth2UserService extends DefaultOAuth2UserService {

    private final OwnerOauth2UserService ownerOauth2UserService;
    private final CustomerOauth2UserService customerOauth2UserService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = req.getSession();

        String uri = (String) session.getAttribute("requestUri");

        if (uri.startsWith("/owner")) {
            return ownerOauth2UserService.loadUser(userRequest);
        } else {
            return customerOauth2UserService.loadUser(userRequest);
        }
    }
}
