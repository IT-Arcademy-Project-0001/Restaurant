package com.project.Restaurant.Board.Post;



import com.project.Restaurant.Board.CommentAnswer.AnswerForm;
import com.project.Restaurant.Board.CommentAnswer.AnswerService;
import com.project.Restaurant.Board.PostComment.Comment;
import com.project.Restaurant.Board.PostComment.CommentForm;
import com.project.Restaurant.Board.PostComment.CommentService;
import com.project.Restaurant.Member.consumer.Customer;
import com.project.Restaurant.Member.consumer.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final CustomerService customerService;
    private final CommentService commentService;
    private final AnswerService answerService;

    @GetMapping("/list/{type}")
    public String list(Model model,
                       @PathVariable String type,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw){
        int category = switch (type) {
            case "notice" -> PostEnum.NOTICE.getStatus();
            case "free" -> PostEnum.FREE.getStatus();
            case "request" -> PostEnum.REQUEST.getStatus();
            default -> throw new RuntimeException("올바르지 않은 접근입니다.");
        };
        model.addAttribute("boardName", category);
        Page<Post> paging = postService.getList(category, page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);

        return "Board/post_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable Long id, CommentForm commentForm, AnswerForm answerForm){
        Post post = this.postService.getPost(id);
        Comment comment = this.commentService.getComment(id);
        model.addAttribute("post", post);
//        model.addAttribute("commentForm", commentForm);
//        model.addAttribute("comment", comment);
//        model.addAttribute("answerForm", answerForm);

        return "Board/post_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create/{type}")
    public String showCreate(@PathVariable String type, PostForm postForm, Model model) {
        switch (type) {
            case "notice" -> model.addAttribute("boardName", "공지사항 작성");
            case "free" -> model.addAttribute("boardName", "자유게시판 작성");
            case "request" -> model.addAttribute("boardName", "고객요청사항 작성");
            default -> throw new RuntimeException("올바르지 않은 접근입니다.");
        }

        return "Board/post_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{type}")
    public String postCreate(@Valid PostForm postForm,@PathVariable String type , BindingResult bindingResult,
                             Principal principal) {
        if (bindingResult.hasErrors()) {
            // 유효성 검사 실패 시 에러 메시지를 모델에 추가
            return "Board/post_form";
        }
        int category = switch (type) {
            case "notice" -> PostEnum.NOTICE.getStatus();
            case "free" -> PostEnum.FREE.getStatus();
            case "request" -> PostEnum.REQUEST.getStatus();
            default -> throw new RuntimeException("올바르지 않은 접근입니다.");
        };

        Customer customer = this.customerService.findByusername(principal.getName());
        this.postService.create(postForm.getTitle(), postForm.getContent(), customer, category);
        return "redirect:/post/list/%s".formatted(type);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String postModify(PostForm postForm, @PathVariable("id") Long id, Principal principal, Model model) {
        Post post = this.postService.getPost(id);
        if(!post.getCustomer().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        switch (post.getCategoryAsEnum()) {
            case NOTICE -> model.addAttribute("boardName", "질문과답변 수정");
            case FREE -> model.addAttribute("boardName", "자유게시판 수정");
            case REQUEST -> model.addAttribute("boardName", "버그및건의 수정");
            default -> throw new RuntimeException("올바르지 않은 접근입니다.");
        }

        postForm.setTitle(post.getTitle());
        postForm.setContent(post.getContent());
        return "Board/post_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String postModify(@Valid PostForm postForm, BindingResult bindingResult,
                             Principal principal, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "Board/post_form";
        }
        Post post = this.postService.getPost(id);
        if (!post.getCustomer().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.postService.modify(post, postForm.getTitle(), postForm.getContent());
        return String.format("redirect:/post/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String postDelete(Principal principal, @PathVariable("id") Long id) {
        Post post = this.postService.getPost(id);
        if (!post.getCustomer().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.postService.delete(post);
        return "redirect:/post/list";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/likes/{id}")
    public String postLikes(Principal principal, @PathVariable("id") Long id) {
        Post post = this.postService.getPost(id);
        Customer customer = this.customerService.findByusername(principal.getName());
        this.postService.likes(post, customer);
        return String.format("redirect:/post/detail/%s", id);
    }
}
