package com.project.Restaurant;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

  @GetMapping("/main")
  public String main() {
    return "main";
  }

  @GetMapping("/")
  public String root() {
    return "redirect:/main";
  }

  @GetMapping("/guide")
  public String guide() {
    return "guide";
  }

  @GetMapping("/member/login")
  public String login() {
    return "login";
  }

  @GetMapping("/member/signup")
  public String membersignup() {
    return "signup";
  }

  @GetMapping("/food/regist")
  public String foodregist() {
    return "foodregist";
  }

  @GetMapping("/food/search")
  public String foodsearch() {
    return "foodsearch";
  }

  @GetMapping("/reservation")
  public String reservation() {
    return "reservation";
  }

  @GetMapping("/community")
  public String community() {
    return "community";
  }

}
