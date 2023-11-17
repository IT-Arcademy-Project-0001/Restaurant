package com.project.Restaurant.PostComment;

import com.project.Restaurant.Post.Post;
import com.project.Restaurant.Post.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class PostCommentController {

    private final PostCommentService postCommentService;
    private final PostService postService;


    @PostMapping("/create/{id}")
    public String createComment(Model model, @PathVariable("id") Long id,
                                @Valid PostCommentForm postCommentForm, BindingResult bindingResult){
        Post post = this.postService.getPost(id);
        model.addAttribute("post", post);
        model.addAttribute("postCommentForm", postCommentForm);


        if (bindingResult.hasErrors()) {
            return "post_detail";
        }

        this.postCommentService.create(post, postCommentForm.getContent());
        return String.format("redirect:/post/detail/%s", id);
    }
}

