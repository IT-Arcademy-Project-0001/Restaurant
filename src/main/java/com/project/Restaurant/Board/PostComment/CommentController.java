package com.project.Restaurant.Board.PostComment;


import com.project.Restaurant.Board.Post.Post;
import com.project.Restaurant.Board.Post.PostService;
import com.project.Restaurant.Member.consumer.Customer;
import com.project.Restaurant.Member.consumer.CustomerService;
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


@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    private final PostService postService;
    private final CustomerService customerService;


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createComment(Model model, @PathVariable("id") Long id,
                                @Valid CommentForm commentForm, BindingResult bindingResult, Principal principal){

        Post post = this.postService.getPost(id);
        Customer customer = this.customerService.findByusername(principal.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            return "Board/post_detail";
        }

        Comment comment = this.commentService.createComment(post,
                commentForm.getContent(), customer);
        return String.format("redirect:/post/detail/%s#postcomment_%s",
                comment.getPost().getId(), comment.getId());
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String commentModify(CommentForm commentForm, @PathVariable("id") Long id, Principal principal) {
        Comment comment = this.commentService.getComment(id);
        if (!comment.getCustomer().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        commentForm.setContent(comment.getContent());
        return "Board/postcomment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String commentModify(@Valid CommentForm commentForm, BindingResult bindingResult,
                                @PathVariable("id") Long id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "Board/postcomment_form";
        }
        Comment comment = this.commentService.getComment(id);
        if (!comment.getCustomer().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.commentService.modifyComment(comment, commentForm.getContent());
        return String.format("redirect:/post/detail/%s#postcomment_%s",
                comment.getPost().getId(), comment.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String commentDelete(Principal principal, @PathVariable("id") Long id) {
        Comment comment = this.commentService.getComment(id);
        if (!comment.getCustomer().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.commentService.deleteComment(comment);
        return String.format("redirect:/post/detail/%s", comment.getPost().getId());
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/likes/{id}")
    public String commentVote(Principal principal, @PathVariable("id") Long id) {
        Comment comment = this.commentService.getComment(id);
        Customer customer = this.customerService.findByusername(principal.getName());
        this.commentService.likesComment(comment, customer);
        return String.format("redirect:/post/detail/%s#postcomment_%s",
                comment.getPost().getId(), comment.getId());
    }
}

