package com.project.Restaurant.Board.CommentAnswer;


import com.project.Restaurant.Board.Post.Post;
import com.project.Restaurant.Board.Post.PostService;
import com.project.Restaurant.Board.PostComment.Comment;
import com.project.Restaurant.Board.PostComment.CommentForm;
import com.project.Restaurant.Board.PostComment.CommentService;
import com.project.Restaurant.Member.consumer.Customer;
import com.project.Restaurant.Member.consumer.CustomerService;
import jakarta.persistence.GeneratedValue;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {

    private final AnswerService answerService;

    private final PostService postService;

    private final CustomerService customerService;

    private final CommentService commentService;
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @RequestParam Long postId, @PathVariable("id") Long commentId,
                                @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal, CommentForm commentForm){

        Comment comment = this.commentService.getComment(commentId);
        Post post = this.postService.getPost(postId);
        Customer customer = this.customerService.findByusername(principal.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            model.addAttribute("targetComment", comment);
            return "Board/post_detail";
        }

        Answer answer = this.answerService.createAnswer(post, comment,
                answerForm.getContent(), customer);


        model.addAttribute("post", post);
//        model.addAttribute("targetComment", comment);
//        model.addAttribute("commentForm", commentForm);
//        model.addAttribute("answerForm", answerForm);
        model.addAttribute("answer", answer);



        return "Board/post_detail";

//        return String.format("redirect:/post/detail/%s#answer_%s",
//                answer.getComment().getId(), answer.getId());
    }

}
