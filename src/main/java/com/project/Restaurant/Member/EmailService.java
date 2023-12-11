package com.project.Restaurant.Member;

import com.project.Restaurant.Member.consumer.Customer;
import com.project.Restaurant.Member.consumer.CustomerRepository;
import com.project.Restaurant.Member.owner.Owner;
import com.project.Restaurant.Member.owner.OwnerRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final CustomerRepository customerRepository;
    private final OwnerRepository ownerRepository;

    public void sendEmailCustomer(String username) throws MessagingException {
        Customer customer = customerRepository.findByusername(username).get();
        String code = customer.getCode();
        String authority = customer.getAuthority();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        String link = "http://localhost:8900/member/welcome/" + authority + "/" + username + "/" + code;

        helper.setTo(customer.getEmail());
        helper.setSubject("회원가입 인증메일 입니다."); // 이메일 제목
        helper.setText("<p>회원가입 인증절차를 완료하려면 다음 링크를 클릭하세요 : <a href='" + link + "'>회원가입 인증</a></p>",
                true);

        javaMailSender.send(message); // 이메일 전송
    }

    public void sendEmailOwner(String username) throws MessagingException {
        Owner owner = ownerRepository.findByusername(username).get();
        String code = owner.getCode();
        String authority = owner.getAuthority();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        String link = "http://localhost:8900/member/welcome/" + authority + "/" + username + "/" + code;

        helper.setTo(owner.getEmail());
        helper.setSubject("회원가입 인증메일 입니다."); // 이메일 제목
        helper.setText("<p>회원가입 인증절차를 완료하려면 다음 링크를 클릭하세요 : <a href='" + link + "'>회원가입 인증</a></p>",
                true);

        javaMailSender.send(message); // 이메일 전송
    }

    public void sendResetCodeOwner(Owner owner) throws MessagingException {
        String code = owner.getCode();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        String link = "http://localhost:8900/member/welcome/" + "/" + owner.getUsername() + "/" + code;

        helper.setTo(owner.getEmail());
        helper.setSubject("비밀번호 변경 메일입니다."); // 이메일 제목
        helper.setText("<p>비밀번호를 변경하시려면 다음 링크를 클릭하세요 : <a href='" + link + "'>비밀번호 변경</a></p>",
                true);

        javaMailSender.send(message); // 이메일 전송
    }
    public void sendResetCodeCustomer(Customer customer) throws MessagingException {
        String code = customer.getCode();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        String link = "http://localhost:8900/member/welcome/" + "/" + customer.getUsername() + "/" + code;

        helper.setTo(customer.getEmail());
        helper.setSubject("비밀번호 변경 메일입니다."); // 이메일 제목
        helper.setText("<p>비밀번호를 변경하시려면 다음 링크를 클릭하세요 : <a href='" + link + "'>비밀번호 변경</a></p>",
                true);

        javaMailSender.send(message); // 이메일 전송
    }
}
