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
    return "Guide/guide";
  }

}
