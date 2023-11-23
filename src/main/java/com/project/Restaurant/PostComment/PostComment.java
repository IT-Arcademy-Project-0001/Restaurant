package com.project.Restaurant.PostComment;

import com.project.Restaurant.Member.consumer.Customer;
import com.project.Restaurant.Post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class PostComment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 200)
  private String content;

  private LocalDateTime localDateTime;

  @ManyToOne
  private Customer author;

  @ManyToOne
  private Post post;

  private LocalDateTime modifyDate;
}
