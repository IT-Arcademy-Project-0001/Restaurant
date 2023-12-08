package com.project.Restaurant.Board.CommentAnswer;


import com.project.Restaurant.Board.Post.Post;
import com.project.Restaurant.Board.PostComment.Comment;
import com.project.Restaurant.Member.consumer.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String content;

    private LocalDateTime localDateTime;

    @ManyToOne
    private Customer customer;

    private LocalDateTime modifyDate;

    @ManyToOne
    private Post post;

    @ManyToOne
    private Comment comment;


}
