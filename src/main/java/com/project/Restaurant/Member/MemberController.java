package com.project.Restaurant.Member;

import com.project.Restaurant.Member.consumer.Customer;
import com.project.Restaurant.Member.consumer.CustomerService;
import com.project.Restaurant.Member.owner.Owner;
import com.project.Restaurant.Member.owner.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {
    private final CustomerService customerService;
    private final OwnerService ownerService;

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

    @GetMapping("/userNameEmail")
    public String usernameEmail() {
        return "member/username_email_form";
    }

    @GetMapping("/welcome/{authority}/{username}/{code}")
    public String welcome(@PathVariable("code") String code,
                          @PathVariable("username") String username,
                          @PathVariable("authority") String authority,
                          Model model) {
        if (authority.equals("소비자")) {
            Customer customer = customerService.findByusername(username);
            if (customer == null) {
                String msg = "잘못된 접근입니다.";
                model.addAttribute("msg", msg);
                return "member/message";
            }
            if (customer.getCode().equals(code)) {
                customerService.customerActivation(customer, true);
                return "member/welcomeForm";
            } else {
                String msg = "인증에 실패했습니다";
                model.addAttribute("msg", msg);
                return "member/message";
            }
        } else {
            Owner owner = ownerService.findByusername(username);
            if (owner == null) {
                String msg = "잘못된 접근입니다.";
                model.addAttribute("msg", msg);
                return "member/message";
            }
            if (owner.getCode().equals(code)) {
                ownerService.ownerActivation(owner, true);
                return "member/welcomeForm";
            } else {
                String msg = "인증에 실패했습니다";
                model.addAttribute("msg", msg);
                return "member/message";
            }
        }
    }

    @GetMapping("/profileInfo")
    public String memberProfileInfo(Model model, Principal principal) {
        Customer customer = customerService.findByusername(principal.getName());
        String photo = customer.getPhoto();
        model.addAttribute("photo", photo);
        return "member/member_profileInfo";
    }
}