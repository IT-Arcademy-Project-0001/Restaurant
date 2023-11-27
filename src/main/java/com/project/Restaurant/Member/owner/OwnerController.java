package com.project.Restaurant.Member.owner;

import com.project.Restaurant.Member.MemberCreateForm;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/owner")
public class OwnerController {

  private final OwnerService ownerService;

  @PostMapping("/signup")
  public String signup(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult) {
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
    return "redirect:/member/login";
  }

  @GetMapping("/profile")
  public String customerProfile(Model model, Principal principal) {
    Owner owner = ownerService.getUser(principal.getName());
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    GrantedAuthority authority = authentication.getAuthorities().iterator().next();
    model.addAttribute("member", owner);
    model.addAttribute("authority", authority);
    return "member/member_profile";
  }

  @PostMapping("/findusername")
  public String findusername(String email, Model model) {
    Owner targetOwner = ownerService.findByEmail(email);
    if (targetOwner == null) {
      String message = "이메일을 학인해주세요";
      model.addAttribute("message", message);
      return "member/find_username_form";
    } else {
      model.addAttribute("targetMember", targetOwner);
      return "member/show_username";
    }
  }
}
