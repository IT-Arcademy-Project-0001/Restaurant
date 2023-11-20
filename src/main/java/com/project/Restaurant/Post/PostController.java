package com.project.Restaurant.Post;



import com.project.Restaurant.PostComment.PostCommentForm;
import com.project.Restaurant.PostComment.PostCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page){
        Page<Post> paging = this.postService.getList(page);
        model.addAttribute("paging", paging);
        return "post_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable Long id, PostCommentForm postCommentForm){
        Post post = this.postService.getPost(id);
        model.addAttribute("post", post);
        return "post_detail";
    }

    @GetMapping("/create")
    public String postCreate(PostForm postForm){
        return "post_form";
    }

    @PostMapping("/create")
    public String postCreate(@Valid PostForm postForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 유효성 검사 실패 시 에러 메시지를 모델에 추가
            return "post_form";
        }

        this.postService.create(postForm.getTitle(), postForm.getContent());
        return "redirect:/post/list";
    }
}
