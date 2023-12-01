package com.project.Restaurant.CommentAnswer;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/answer")
public class AnswerController {

    private final AnswerRepository answerRepository;

    @GetMapping("/create/{id}")
    public String createAnswer(){


        return "Post/post_detail";
    }
}
