package com.project.Restaurant.PostComment;

import com.project.Restaurant.DataNotFoundException;
import com.project.Restaurant.Member.consumer.Customer;
import com.project.Restaurant.Post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;

    public PostComment create(Post post, String content, Customer author){

        PostComment postComment = new PostComment();
        postComment.setContent(content);
        postComment.setLocalDateTime(LocalDateTime.now());
        postComment.setPost(post);
        postComment.setAuthor(author);

        this.postCommentRepository.save(postComment);
        return postComment;
    }

    public PostComment getpostComment(Long id) {
        Optional<PostComment> answer = this.postCommentRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
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
