package com.project.Restaurant.Member.owner;

import com.project.Restaurant.Member.EmailService;
import com.project.Restaurant.Member.MemberCreateForm;
import com.project.Restaurant.Member.PasswordResetForm;
import com.project.Restaurant.Member.consumer.Customer;
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
@RequestMapping("/owner")
public class OwnerController {

    private final OwnerService ownerService;
    private final EmailService emailService;

    @GetMapping("/login")
    public String login() {
        return "member/loginForm/ownerLoginForm";
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/member/signup_form";
        }
        if (!memberCreateForm.getPassword().equals(memberCreateForm.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "passwordInCorrect",
                    "패스워드가 일치하지 않습니다.");
            return "/member/signup_form";
        }
        try {
            ownerService.create(memberCreateForm.getUsername(), memberCreateForm.getPassword(),
                    memberCreateForm.getNickname(), memberCreateForm.getEmail());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "/member/signup_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "/member/signup_form";
        }
        emailService.sendEmailOwner(memberCreateForm.getUsername());
        String msg = "인증메일이 발송되었습니다";
        model.addAttribute("msg", msg);
        return "member/message";
    }

    @GetMapping("/profile")
    public String customerProfile(Model model, Principal principal) {
        Owner owner = ownerService.findByusername(principal.getName());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        GrantedAuthority authority = authentication.getAuthorities().iterator().next();
        model.addAttribute("member", owner);
        model.addAttribute("authority", authority);
        return "member/member_profile";
    }

    @GetMapping("/profile/test")
    public String test() {

        return "member/member_profile_layout";
    }

    @PostMapping("/findusername")
    public String findusername(String email, Model model) {
        Owner targetOwner = ownerService.findByEmail(email);
        if (targetOwner == null) {
            String message = "이메일을 확인해주세요";
            model.addAttribute("message", message);
            return "member/find_username_form";
        } else {
            model.addAttribute("targetMember", targetOwner);
            return "member/show_username";
        }
    }

    @PostMapping("/userNameEmail")
    public String usernameEmail(String username, String email, Model model) {
        Owner targetOwner = ownerService.findByusername(username);
        String message = "아이디 또는 이메일을 확인해주세요";
        if (targetOwner == null) {
            model.addAttribute("message", message);
            return "member/username_email_form";
        } else {
            if (!targetOwner.getEmail().equals(email)) {
                model.addAttribute("message", message);
                return "member/username_email_form";
            } else {
                String msg = "이메일이 전송되었습니다";
                model.addAttribute("msg", msg);
                emailService.sendResetCodeOwner(targetOwner);
                return "member/message";
            }
        }
    }

    @GetMapping("/resetPassword/{username}/{code}")
    public String resetPasswordForm(@PathVariable("code") String code,
                                    @PathVariable("username") String username,
                                    PasswordResetForm passwordResetForm,
                                    Model model) {
        Owner targetOwner = ownerService.findByusername(username);
        if (targetOwner == null) {
            String msg = "잘못된 접근입니다.";
            model.addAttribute("msg", msg);
            return "member/message";
        }
        if (targetOwner.getCode().equals(code)) {
            model.addAttribute("targetMember", targetOwner);
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
        Owner targetOwner = ownerService.findByusername(targetMemberUsername);
        if (bindingResult.hasErrors()) {
            model.addAttribute("targetMember", targetOwner);
            return "member/reset_password_form";
        }
        if (!passwordResetForm.getPassword().equals(passwordResetForm.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "passwordInCorrect",
                    "패스워드가 일치하지 않습니다.");
            model.addAttribute("targetMember", targetOwner);
            return "member/reset_password_form";
        }
        try {
            ownerService.resetPassword(targetOwner, passwordResetForm.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("PasswordRestFailed", e.getMessage());
            model.addAttribute("targetMember", targetOwner);
            return "member/reset_password_form";
        }
        return "member/loginForm/login_form";
    }
}
