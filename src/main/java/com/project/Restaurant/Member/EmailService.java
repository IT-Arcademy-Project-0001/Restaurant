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
        SimpleMailMessage message = new SimpleMailMessage();
        String code = customer.getCode();
        String authority = customer.getAuthority();
//        message.setSubject("환영합니다.");
//        message.setText("http://localhost:8900/member/welcome/" + authority + "/" + username + "/" + code);
//        message.setTo(customer.getEmail());
//        javaMailSender.send(message);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(customer.getEmail());
        helper.setSubject("링크로보내지는지 확인용"); // 이메일 제목
        String link = "http://localhost:8900/member/welcome/" + authority + "/" + username + "/" + code;
        helper.setText("<p>비밀번호를 변경하려면 다음 링크를 클릭하세요: <a href='" + link + "'>비밀번호 변경하기</a></p>",
                true); // HTML 포맷으로 설정

        javaMailSender.send(mimeMessage); // 이메일 전송
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
