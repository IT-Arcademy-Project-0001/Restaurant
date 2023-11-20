package com.project.Restaurant.PostComment;

import com.project.Restaurant.Member.Member;
import com.project.Restaurant.Member.MemberService;
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
    private final MemberService memberService;



    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createComment(Model model, @PathVariable("id") Long id,
                                @Valid PostCommentForm postCommentForm, BindingResult bindingResult,
                                Principal principal){
        Post post = this.postService.getPost(id);
        Member member = this.memberService.getMemberByUsername(principal.getName());
        model.addAttribute("post", post);
        model.addAttribute("postCommentForm", postCommentForm);


        if (bindingResult.hasErrors()) {
            return "post_detail";
        }

        this.postCommentService.create(post, postCommentForm.getContent(), member);
        return String.format("redirect:/post/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String commentModify(PostCommentForm postCommentForm, @PathVariable("id") Long id, Principal principal) {
        PostComment postComment = this.postCommentService.getComment(id);
        if (!postComment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        postCommentForm.setContent(postComment.getContent());
        return "postcomment_form";

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String commentModify(@Valid PostCommentForm postCommentForm, BindingResult bindingResult,
                               @PathVariable("id") Long id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "postcomment_form";
        }
        PostComment postComment = this.postCommentService.getComment(id);
        if (!postComment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.postCommentService.modify(postComment, postCommentForm.getContent());
        return String.format("redirect:/post/detail/%s", postComment.getPost().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String commentDelete(Principal principal, @PathVariable("id") Long id) {
        PostComment postComment = this.postCommentService.getComment(id);
        if (!postComment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.postCommentService.delete(postComment);
        return String.format("redirect:/post/detail/%s", postComment.getPost().getId());
    }
}

