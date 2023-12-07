package com.project.Restaurant.Board.PostComment;

import com.project.Restaurant.Board.Post.Post;
import com.project.Restaurant.DataNotFoundException;
import com.project.Restaurant.Member.consumer.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment createComment(Post post, String content, Customer customer) {


        Comment comment = new Comment();
        comment.setContent(content);
        comment.setLocalDateTime(LocalDateTime.now());
        comment.setPost(post);
        comment.setCustomer(customer);
        this.commentRepository.save(comment);
        return comment;
    }

    public Comment getComment(Long id) {
        System.out.println( "=======================" + "시작" + "=======================");
        Optional<Comment> commentOptional = this.commentRepository.findById(id);
        System.out.println( "=======================" + commentOptional+ "=======================");
        if(commentOptional.isEmpty()){
            return null;
        }

        return commentOptional.get();
    }



    public void modifyComment(Comment comment, String content) {
        comment.setContent(content);
        comment.setModifyDate(LocalDateTime.now());
        this.commentRepository.save(comment);
    }

    public void deleteComment(Comment comment) {
        this.commentRepository.delete(comment);
    }

    public void likesComment(Comment comment, Customer customer) {
        comment.getLikes().add(customer);
        this.commentRepository.save(comment);
    }
}
