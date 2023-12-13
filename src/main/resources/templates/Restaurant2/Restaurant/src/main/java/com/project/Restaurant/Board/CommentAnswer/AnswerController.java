package com.project.Restaurant.Board.CommentAnswer;


import com.project.Restaurant.Board.Post.Post;
import com.project.Restaurant.Board.Post.PostService;
import com.project.Restaurant.Board.PostComment.Comment;
import com.project.Restaurant.Board.PostComment.CommentForm;
import com.project.Restaurant.Board.PostComment.CommentService;
import com.project.Restaurant.Member.MemberRole;
import com.project.Restaurant.Member.consumer.Customer;
import com.project.Restaurant.Member.consumer.CustomerService;
import jakarta.persistence.GeneratedValue;
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
import java.util.Optional;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {

    private final AnswerService answerService;

    private final PostService postService;

    private final CustomerService customerService;

    private final CommentService commentService;


    private final int PAGESIZE = 5;

    @GetMapping("/comment/{id}")
    public String showAnswers(Model model, @ModelAttribute AnswerForm answerForm,
                              @RequestParam(defaultValue = "0") Integer answerPage,
                              @PathVariable Long id, @RequestParam(required = false) Long postId) {
        Post post;


        post = postService.getPost(postId);
        model.addAttribute("post", post);
        Comment comment = commentService.getComment(id);
        model.addAttribute("comment", comment);
        Page<Answer> answerPaging = answerService.getAnswerPageByComment(answerPage, comment);
        model.addAttribute("commentAnswerPaging", answerPaging);
        model.addAttribute("totalCount", answerPaging.getTotalElements());
        return "Board/comment_answer";
    }

    @GetMapping("/commentPage/{id}")
    public String showAnswersPage(Model model, @ModelAttribute AnswerForm answerForm,
                              @RequestParam(defaultValue = "0") Integer answerPage,
                              @PathVariable Long id, @RequestParam(required = false) Long postId) {
        Post post;


        post = postService.getPost(postId);
        model.addAttribute("post", post);
        Comment comment = commentService.getComment(id);
        model.addAttribute("comment", comment);
        Page<Answer> answerPaging = answerService.getAnswerPageByComment(answerPage, comment);
        model.addAttribute("commentAnswerPaging", answerPaging);
        model.addAttribute("totalCount", answerPaging.getTotalElements());

        return "Board/comment_answer_fragment :: #comment-answer-list";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/comment")
    public String create(Model model, @ModelAttribute AnswerForm answerForm, Principal principal,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "Board/post_detail";
        }


        Customer customer = customerService.findByusername(principal.getName());
        String content = answerForm.getAnswerContents().trim();
        Post post = postService.getPost(answerForm.getPostId());

        int lastPage;

        Comment comment = commentService.getComment(answerForm.getCommentId());
        Answer answer = answerService.createByComment(content, answerForm.getSecret(), customer, comment);


        lastPage = answerService.getLastPageNumberByComment(comment);
        Page<Answer> answerPaging = answerService.getAnswerPageByComment(lastPage, comment);
        model.addAttribute("commentAnswerPaging", answerPaging);
        model.addAttribute("post", post);
        model.addAttribute("comment", comment);
        model.addAttribute("totalCount", answerPaging.getTotalElements());
        return "Board/comment_answer :: #comment-answer-list";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/reply/create/comment")
    public String replyCreate(Model model, @ModelAttribute AnswerForm answerForm,
                              BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "Board/post_detail";
        }


        Post post = postService.getPost(answerForm.getPostId());
        Customer customer = customerService.findByusername(principal.getName());

        // 부모 댓글 찾아오기
        Answer parent = answerService.getAnswer(answerForm.getParentId());

        if (parent == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 댓글을 찾을 수 없습니다");
        }

        model.addAttribute("post", post);

        int page = 0;
        Page<Answer> paging;

        Comment comment = commentService.getComment(answerForm.getCommentId());
        model.addAttribute("comment", comment);
        Answer answer = answerService.createReplyAnswerByComment(answerForm.getAnswerContents(),
                answerForm.getSecret(), customer, comment, parent);
        // 부모 댓글이 있는 페이지로 가야하므로, 부모의 페이지를 구해옴
        page = answerService.getPageNumberByComment(comment, answer, PAGESIZE);
        paging = answerService.getAnswerPageByComment(page, comment);
        model.addAttribute("answer", answer);
        model.addAttribute("comment", comment);
        model.addAttribute("commentAnswerPaging", paging);
        // 전체 댓글 수 갱신
        model.addAttribute("totalCount", paging.getTotalElements());
        return "Board/comment_answer :: #comment-answer-list";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/comment")
    public String modify(AnswerForm answerForm, Model model, Principal principal) {
        Post post = postService.getPost(answerForm.getPostId());
        Customer customer = customerService.findByusername(principal.getName());


        if (!(customer.isAdmin()) && (customer.getId() != answerForm.getAnswerWriter())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다");
        }



        Answer answer = answerService.getAnswer(answerForm.getId());

        answerService.modify(answer, answerForm.getAnswerContents().trim(), answerForm.getSecret());

        Page<Answer> paging;
        int page = 0;

        Comment comment = commentService.getComment(answerForm.getCommentId());
        model.addAttribute("post", post);
        model.addAttribute("comment", comment);
        page = answerService.getPageNumberByComment(comment, answer, PAGESIZE);
        paging = answerService.getAnswerPageByComment(page, comment);
        model.addAttribute("commentAnswerPaging", paging);
        return "Board/comment_answer :: #comment-answer-list";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/comment")
    public String delete(Model model, AnswerForm answerForm, Principal principal) {
        Post post = postService.getPost(answerForm.getPostId());
        Customer customer = customerService.findByusername(principal.getName());

        // 관리자가 아니거나 현재 로그인한 사용자가 작성한 댓글이 아니면 삭제 불가
        if (!(customer.isAdmin()) && (customer.getId() != answerForm.getAnswerWriter())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다");
        }




        Answer answer = answerService.getAnswer(answerForm.getId());

        int page = 0;

        Comment comment = null;

        comment = commentService.getComment(answerForm.getCommentId());
        page = answerService.getPageNumberByComment(comment, answer, PAGESIZE);
        // 부모(댓글)이 있을 경우 연관관계 끊어주기 -> 삭제되더라도 GET 등으로 새로 요청을 보내는 것이 아니기에
        // 이 작업은 꼭 해줘야 대댓글 리스트도 수정된다!

        // 부모댓글이 삭제 되지 않았다면 연관관계 끊어주기만 하면 됨
        // => Ajax 비동기 리스트화를 위해 리스트에서 명시적 삭제
        if (answer.getParent() != null && !answer.getParent().isDeleted()) {
            answer.getParent().getChildren().remove(answer);
        }
        // 부모댓글이 삭제 상태이고 부모의 자식 댓글이 본인 포함 2개 이상이라면
        // 자식 댓글의 삭제가 부모 댓글 객체 삭제에 영향을 주지 않으니 연관관계만 끊어주기
        // => Ajax 비동기 리스트화를 위해 리스트에서 명시적 삭제
        else if (answer.getParent() != null && answer.getParent().isDeleted()
                && answer.getParent().getChildren().size() > 1) {
            answer.getParent().getChildren().remove(answer);
        }

        answerService.delete(answer);

        model.addAttribute("post", post);
        Page<Answer> paging;

        paging = answerService.getAnswerPageByComment(page, comment);
        if((page !=0 && paging.getNumberOfElements() == 0))
            paging = answerService.getAnswerPageByComment(page-1, comment);

        model.addAttribute("comment", comment);
        model.addAttribute("commentAnswerPaging", paging);
        // 전체 댓글 수 갱신
        model.addAttribute("totalCount", paging.getTotalElements());
        return "Board/comment_answer :: #comment-answer-list";
    }

}
