package com.project.Restaurant.Board.PostComment;

import com.project.Restaurant.Board.CommentAnswer.Answer;
import com.project.Restaurant.Board.Post.Post;
import com.project.Restaurant.Member.consumer.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Comment {

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


  private LocalDateTime modifyDate;

  @ManyToMany
  Set<Customer> likes;


  @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
  private List<Answer> answerList;

}
