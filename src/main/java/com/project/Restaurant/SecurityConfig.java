package com.project.Restaurant;


import com.project.Restaurant.Member.consumer.CustomerDetailsService;
import com.project.Restaurant.Member.consumer.CustomerRepository;
import com.project.Restaurant.Member.owner.OwnerDetailsService;
import com.project.Restaurant.Member.owner.OwnerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomerRepository customerRepository;
    private final OwnerRepository ownerRepository;

    @Bean
    @Order(1)
    @Transactional
    public SecurityFilterChain customerFilterChain(HttpSecurity http) throws Exception {
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
                        .failureUrl("/member/login?error=true")
                        .permitAll())
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true));
        return http.build();
    }

    @Bean
    @Order(2)
    @Transactional
    public SecurityFilterChain ownerFilterChain2(HttpSecurity http) throws Exception {
        http
                .securityMatcher(new AntPathRequestMatcher("/**"))
                .authenticationProvider(customerDaoAuthenticationProvider())
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/customer/login")
                                .loginProcessingUrl("/customer/login")
                                .defaultSuccessUrl("/")
                                .failureUrl("/member/login?error=true")
                                .permitAll())
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
