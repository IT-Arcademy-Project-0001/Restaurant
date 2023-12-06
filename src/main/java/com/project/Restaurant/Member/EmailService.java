package com.project.Restaurant.Member;

import com.project.Restaurant.Member.consumer.Customer;
import com.project.Restaurant.Member.consumer.CustomerRepository;
import com.project.Restaurant.Member.owner.Owner;
import com.project.Restaurant.Member.owner.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final CustomerRepository customerRepository;
    private final OwnerRepository ownerRepository;

    public void sendEmailCustomer(String username) {
        Customer customer = customerRepository.findByusername(username).get();
        SimpleMailMessage message = new SimpleMailMessage();
        String code = customer.getCode();
        String authority = customer.getAuthority();
        message.setSubject("환영합니다.");
        message.setText("http://localhost:8900/member/welcome/" + authority + "/" + username + "/" + code);
        message.setTo(customer.getEmail());
        javaMailSender.send(message);
    }

    public void sendEmailOwner(String username) {
        Owner owner = ownerRepository.findByusername(username).get();
        SimpleMailMessage message = new SimpleMailMessage();
        String code = owner.getCode();
        String authority = owner.getAuthority();
        message.setSubject("환영합니다.");
        message.setText("http://localhost:8900/member/welcome/" + authority + "/" + username + "/" + code);
        message.setTo(owner.getEmail());
        javaMailSender.send(message);
    }

    public void sendResetCodeOwner(Owner owner) {
        SimpleMailMessage message = new SimpleMailMessage();
        String code = owner.getCode();
        message.setSubject("패스워드 변경 메일입니다.");
        message.setText("http://localhost:8900/owner/resetPassword/" + owner.getUsername() + "/" + code);
        message.setTo(owner.getEmail());
        javaMailSender.send(message);
    }
    public void sendResetCodeCustomer(Customer customer) {
        SimpleMailMessage message = new SimpleMailMessage();
        String code = customer.getCode();
        message.setSubject("패스워드 변경 메일입니다.");
        message.setText("http://localhost:8900/customer/resetPassword/" + customer.getUsername() + "/" + code);
        message.setTo(customer.getEmail());
        javaMailSender.send(message);
    }
}
