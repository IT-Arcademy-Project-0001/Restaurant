package com.project.Restaurant;


import com.project.Restaurant.Member.consumer.CustomerDetailsService;
import com.project.Restaurant.Member.consumer.CustomerOauth2UserService;
import com.project.Restaurant.Member.consumer.CustomerRepository;
import com.project.Restaurant.Member.Oauth2UserService;
import com.project.Restaurant.Member.owner.OwnerDetailsService;
import com.project.Restaurant.Member.owner.OwnerOauth2UserService;
import com.project.Restaurant.Member.owner.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomerRepository customerRepository;
    private final OwnerRepository ownerRepository;
    private final CustomerOauth2UserService customerOauth2UserService;
    private final OwnerOauth2UserService ownerOauth2UserService;
    private final Oauth2UserService oauth2UserService;


    @Bean
    @Order(1)
    public SecurityFilterChain ownerFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(new AntPathRequestMatcher("/owner/**"))
                .authenticationProvider(ownerDaoAuthenticationProvider())
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers(new AntPathRequestMatcher("/**"))
                                .permitAll())
                .formLogin(formLogin -> formLogin
                        .loginPage("/owner/login")
                        .defaultSuccessUrl("/")
                        .permitAll())
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true));
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain customerFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(new NegatedRequestMatcher(new AntPathRequestMatcher("/owner/**")))
                .authenticationProvider(customerDaoAuthenticationProvider())
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers(new AntPathRequestMatcher("/**"))
                                .permitAll())
                .formLogin(formLogin -> formLogin
                        .loginPage("/customer/login")
                        .defaultSuccessUrl("/")
                        .permitAll())
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/customer/login")
                        .defaultSuccessUrl("/")
                        .userInfoEndpoint(userInfo -> userInfo.userService(oauth2UserService)))
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
        .logoutSuccessUrl("/")
        .invalidateHttpSession(true));
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider ownerDaoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(new OwnerDetailsService(ownerRepository));
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    DaoAuthenticationProvider customerDaoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(new CustomerDetailsService(customerRepository));
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }
}
