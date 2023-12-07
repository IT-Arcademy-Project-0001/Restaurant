package com.project.Restaurant.Member;

import com.project.Restaurant.Member.consumer.Customer;
import com.project.Restaurant.Member.consumer.CustomerService;
import com.project.Restaurant.Member.owner.Owner;
import com.project.Restaurant.Member.owner.OwnerService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {
    private final CustomerService customerService;
    private final OwnerService ownerService;
    private final PasswordEncoder passwordEncoder;

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

    @GetMapping("/deleteForm")
    public String deleteForm(PasswordResetForm passwordResetForm) {
        return "member/deleteForm";
    }

    @PostMapping("/delete")
    public String delete(Model model, Principal principal, @Valid PasswordResetForm passwordResetForm,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "member/deleteForm";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        GrantedAuthority authority = authentication.getAuthorities().iterator().next();
        String msg = "탈퇴실패";

        if (passwordResetForm.getPassword().equals(passwordResetForm.getPasswordConfirm())) {
            if (authority.getAuthority().equals("사장님")) {
                Owner owner = ownerService.findByusername(principal.getName());
                if (passwordEncoder.matches(passwordResetForm.getPassword(), owner.getPassword())) {
                    ownerService.deleteOwner(owner);
                    return "redirect:/member/logout";
                } else {
                    model.addAttribute("msg", msg);
                    return "member/deleteForm";
                }
            } else {
                Customer customer = customerService.findByusername(principal.getName());
                if (passwordEncoder.matches(passwordResetForm.getPassword(), customer.getPassword())) {
                    customerService.deleteCustomer(customer);
                    return "redirect:/member/logout";
                } else {
                    model.addAttribute("msg", msg);
                    return "member/deleteForm";
                }
            }
        } else {
            model.addAttribute("msg", msg);
            return "member/deleteForm";
        }
    }

    @GetMapping("/profileInfo")
    public String memberProfileInfo(Model model, Principal principal) {
        populateMemberInfo(model, principal);
        return "member/member_profileInfo";
    }

    @GetMapping("/security")
    public String memberSecurity(Model model, Principal principal) {
        populateMemberInfo(model, principal);
        return "member/security";
    }

    private void populateMemberInfo(Model model, Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        GrantedAuthority authority = authentication.getAuthorities().iterator().next();

        if (authority.getAuthority().equals("사장님")) {
            Owner owner = ownerService.findByusername(principal.getName());
            model.addAttribute("member", owner);
        } else {
            Customer customer = customerService.findByusername(principal.getName());
            model.addAttribute("member", customer);
        }
    }
}