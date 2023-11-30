package com.project.Restaurant.Member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {

    @GetMapping("/login")
    public String login() {
        return "member/loginForm/login_form";
    }

    @GetMapping("/signup")
    public String signup(MemberCreateForm memberCreateForm) {
        return "member/signup_form";
    }

    @GetMapping("/findusername")
    public String findusername() {
        return "member/find_username_form";
    }

    @GetMapping("userNameEmail")
    public String usernameEmail() {
        return "member/username_email_form";
    }
}