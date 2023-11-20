package com.project.Restaurant.Member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member create(String username, String email, String password, String memberNickName) {
        Member member = new Member();
        member.setUsername(username);
        member.setEmail(email);
        member.setMemberNickName(memberNickName);
        member.setPassword(passwordEncoder.encode(password));
        member.setSignupDate(LocalDateTime.now());
        member.setAuthority("customer");
        member.setMemberActivation(true);
        memberRepository.save(member);
        return member;
    }

    public Member createSeller(String username, String email, String password, String memberNickName) {
        Member member = new Member();
        member.setUsername(username);
        member.setEmail(email);
        member.setMemberNickName(memberNickName);
        member.setPassword(passwordEncoder.encode(password));
        member.setSignupDate(LocalDateTime.now());
        member.setAuthority("seller");
        member.setMemberActivation(false);
        memberRepository.save(member);
        return member;
    }

    public Member getMemberByUsername(String username) {
        Member member = null;
        for (int i = 0; i < memberRepository.findAll().size(); i++) {
            if (memberRepository.findAll().get(i).getUsername().equals(username)) {
                member = memberRepository.findAll().get(i);
            }
        }
        return member;
    }
}
