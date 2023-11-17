package com.project.Restaurant.PostComment;

import com.project.Restaurant.Post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;

    public void create(Post post, String content){
        PostComment postComment = new PostComment();
        postComment.setContent(content);
        postComment.setLocalDateTime(LocalDateTime.now());
        postComment.setPost(post);

        this.postCommentRepository.save(postComment);
    }
}
