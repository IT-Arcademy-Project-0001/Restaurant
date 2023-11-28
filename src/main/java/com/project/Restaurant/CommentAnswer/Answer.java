package com.project.Restaurant.CommentAnswer;


import com.project.Restaurant.Member.consumer.Customer;
import com.project.Restaurant.Post.Post;
import com.project.Restaurant.PostComment.PostComment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String content;

    private LocalDateTime localDateTime;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Post post;

    @ManyToOne
    private PostComment postComment;

    private LocalDateTime modifyDate;
}
