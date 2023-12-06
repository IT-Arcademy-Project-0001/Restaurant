package com.project.Restaurant.Member.consumer;

import com.project.Restaurant.Member.EmailService;
import com.project.Restaurant.Member.MemberCreateForm;
import com.project.Restaurant.Member.PasswordResetForm;
import com.project.Restaurant.Member.owner.Owner;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final EmailService emailService;

    @GetMapping("/profile/test")
    public String test() {

        return "member/loginForm/member_profile_layout";
    }


    @GetMapping("/login")
    public String login() {
        return "member/loginForm/customerLoginForm";
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "member/signup_form";
        }
        if (!memberCreateForm.getPassword().equals(memberCreateForm.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "passwordInCorrect",
                    "패스워드가 일치하지 않습니다.");
            return "member/signup_form";
        }
        try {
            customerService.create(memberCreateForm.getUsername(), memberCreateForm.getPassword(),
                    memberCreateForm.getNickname(), memberCreateForm.getEmail());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "member/signup_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "member/signup_form";
        }
        emailService.sendEmailCustomer(memberCreateForm.getUsername());
        String msg = "인증메일이 발송되었습니다!";
        model.addAttribute("msg", msg);
        return "member/message";
    }

    @GetMapping("/profile")
    public String customerProfile(Model model, Principal principal) {

        Customer customer = customerService.findByusername(principal.getName());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        GrantedAuthority authority = authentication.getAuthorities().iterator().next();

        model.addAttribute("member", customer);
        model.addAttribute("authority", authority);
        return "member/member_profile";
    }

    @PostMapping("/findusername")
    public String findusername(String email, Model model) {
        Customer targetCustomer = customerService.findByEmail(email);
        if (targetCustomer == null) {
            String message = "이메일을 확인해주세요";
            model.addAttribute("message", message);
            return "member/find_username_form";
        } else {
            model.addAttribute("targetMember", targetCustomer);
            return "member/show_username";
        }
    }

    @PostMapping("/userNameEmail")
    public String usernameEmail(String username, String email, Model model) {
        Customer targetCustomer = customerService.findByusername(username);
        String message = "아이디 또는 이메일을 확인해주세요";
        if (targetCustomer == null) {
            model.addAttribute("message", message);
            return "member/username_email_form";
        } else {
            if (!targetCustomer.getEmail().equals(email)) {
                model.addAttribute("message", message);
                return "member/username_email_form";
            } else {
                String msg = "이메일이 전송되었습니다";
                model.addAttribute("msg", msg);
                emailService.sendResetCodeCustomer(targetCustomer);
                return "member/message";
            }
        }
    }

    @GetMapping("/resetPassword/{username}/{code}")
    public String resetPasswordForm(@PathVariable("code") String code,
                                    @PathVariable("username") String username,
                                    PasswordResetForm passwordResetForm,
                                    Model model) {
        Customer targetCustomer = customerService.findByusername(username);
        if (targetCustomer == null) {
            String msg = "잘못된 접근입니다.";
            model.addAttribute("msg", msg);
            return "member/message";
        }
        if (targetCustomer.getCode().equals(code)) {
            model.addAttribute("targetMember", targetCustomer);
            return "member/reset_password_form";
        } else {
            String msg = "인증에 실패했습니다";
            model.addAttribute("msg", msg);
            return "member/message";
        }
    }

    @PostMapping("/resetPassword")
    public String resetPassword(String targetMemberUsername, @Valid PasswordResetForm passwordResetForm,
                                BindingResult bindingResult, Model model) {
        Customer targetCustomer = customerService.findByusername(targetMemberUsername);
        if (bindingResult.hasErrors()) {
            model.addAttribute("targetMember", targetCustomer);
            return "member/reset_password_form";
        }
        if (!passwordResetForm.getPassword().equals(passwordResetForm.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "passwordInCorrect",
                    "패스워드가 일치하지 않습니다.");
            model.addAttribute("targetMember", targetCustomer);
            return "member/reset_password_form";
        }
        try {
            customerService.resetPassword(targetCustomer, passwordResetForm.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("PasswordRestFailed", e.getMessage());
            model.addAttribute("targetMember", targetCustomer);
            return "member/reset_password_form";
        }
        return "member/loginForm/login_form";
    }
}
