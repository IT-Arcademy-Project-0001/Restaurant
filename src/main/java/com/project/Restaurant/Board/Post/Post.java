package com.project.Restaurant.Board.Post;

import com.project.Restaurant.Board.CommentAnswer.Answer;
import com.project.Restaurant.Member.consumer.Customer;
import com.project.Restaurant.Board.PostComment.Comment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
  private List<Comment> commentList;

  private LocalDateTime modifyDate;

  @ManyToMany
  Set<Customer> likes;

  @Column(columnDefinition = "Integer default 0")
  private Long view;

  private int category;

  public PostEnum getCategoryAsEnum() {
    switch (this.category) {
      case 0:
        return PostEnum.NOTICE;
      case 1:
        return PostEnum.FREE;
      case 2:
        return PostEnum.REQUEST;
      default:
        throw new RuntimeException("올바르지 않은 접근입니다.");
    }
  }

  public String getCategoryAsString() {
    switch (this.category) {
      case 0:
        return "공지사항";
      case 1:
        return "자유게시판";
      case 2:
        return "고격요청사항";
      default:
        throw new RuntimeException("올바르지 않은 접근입니다.");
    }
  }


  @OneToMany(mappedBy = "post")
  private List<Answer> answerList;


}

