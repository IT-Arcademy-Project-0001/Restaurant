package com.project.Restaurant.Post;

import com.project.Restaurant.CommentAnswer.Answer;
import com.project.Restaurant.Member.consumer.Customer;
import com.project.Restaurant.PostComment.PostComment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 30)
  private String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  private LocalDateTime localDateTime;


  @ManyToOne
  private Customer customer;

  @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
  private List<PostComment> postCommentList;

  @OneToMany(cascade = CascadeType.REMOVE)
  private List<Answer> answerList;

  private LocalDateTime modifyDate;

  @ManyToMany
  Set<Customer> likes;

  @Column(columnDefinition = "Integer default 0")
  private Long view;

}

