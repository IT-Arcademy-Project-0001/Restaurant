package com.project.Restaurant.Place.Comment;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/place")
@Controller
public class PlaceOwnerCommentController {

  @GetMapping("/comment")
  public String placecomment(Model model, @PathVariable("categoryId") Integer categoryId, @PathVariable("id") Integer id) {
    return "PlaceSearch/place_review";
  }
}
