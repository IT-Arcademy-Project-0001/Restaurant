package com.project.Restaurant.PostComment;

import com.project.Restaurant.Member.Member;
import com.project.Restaurant.Post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;

    public void create(Post post, String content, Member author){
        PostComment postComment = new PostComment();
        postComment.setContent(content);
        postComment.setLocalDateTime(LocalDateTime.now());
        postComment.setPost(post);
        postComment.setAuthor(author);
        this.postCommentRepository.save(postComment);
    }

    public PostComment getComment(Long id) {
        Optional<PostComment> postComment = this.postCommentRepository.findById(id);
        if (postComment.isPresent()) {
            return postComment.get();
        } else {
            throw new IllegalArgumentException("없는 답변입니다");
        }
    }

    public void modify(PostComment postComment, String content) {
        postComment.setContent(content);
        postComment.setModifyDate(LocalDateTime.now());
        this.postCommentRepository.save(postComment);
    }

    public void delete(PostComment postComment) {
        this.postCommentRepository.delete(postComment);
    }
}
