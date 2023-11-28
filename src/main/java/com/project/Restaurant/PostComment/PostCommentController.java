package com.project.Restaurant.PostComment;

import com.project.Restaurant.Member.consumer.Customer;
import com.project.Restaurant.Member.consumer.CustomerService;
import com.project.Restaurant.Post.Post;
import com.project.Restaurant.Post.PostService;
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
public class PostCommentController {

    private final PostCommentService postCommentService;
    private final PostService postService;
    private final CustomerService customerService;


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createComment(Model model, @PathVariable("id") Long id,
                                @Valid PostCommentForm postCommentForm, BindingResult bindingResult, Principal principal){



        Post post = this.postService.getPost(id);
        Customer customer = this.customerService.findByusername(principal.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            return "Post/post_detail";
        }

        PostComment postComment = this.postCommentService.create(post,
                postCommentForm.getContent(), customer);
        return String.format("redirect:/post/detail/%s#postcomment_%s",
                postComment.getPost().getId(), postComment.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify(PostCommentForm postCommentForm, @PathVariable("id") Long id, Principal principal) {
        PostComment postComment = this.postCommentService.getpostComment(id);
        if (!postComment.getCustomer().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        postCommentForm.setContent(postComment.getContent());
        return "Post/postcomment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid PostCommentForm postCommentForm, BindingResult bindingResult,
                               @PathVariable("id") Long id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "Post/postcomment_form";
        }
        PostComment postComment = this.postCommentService.getpostComment(id);
        if (!postComment.getCustomer().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.postCommentService.modify(postComment, postCommentForm.getContent());
        return String.format("redirect:/post/detail/%s#postcomment_%s",
                postComment.getPost().getId(), postComment.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Long id) {
        PostComment postComment = this.postCommentService.getpostComment(id);
        if (!postComment.getCustomer().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.postCommentService.delete(postComment);
        return String.format("redirect:/post/detail/%s", postComment.getPost().getId());
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/likes/{id}")
    public String answerVote(Principal principal, @PathVariable("id") Long id) {
        PostComment postComment = this.postCommentService.getpostComment(id);
        Customer customer = this.customerService.findByusername(principal.getName());
        this.postCommentService.likes(postComment, customer);
        return String.format("redirect:/post/detail/%s#postcomment_%s",
                postComment.getPost().getId(), postComment.getId());
    }
}

