package com.project.Restaurant.Board.PostComment;

import com.project.Restaurant.Board.CommentAnswer.Answer;
import com.project.Restaurant.Board.Post.Post;
import com.project.Restaurant.Member.consumer.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 200)
  private String content;

  private LocalDateTime localDateTime;

  @CreatedDate
  private LocalDateTime createDate;

  @LastModifiedDate
  private LocalDateTime modifyDate;

  @PrePersist
  public void prePersist() {
    this.modifyDate = null; // 객체 생성 시 처음에 수정일 null값으로 설정
  }

  @ManyToOne
  private Customer customer;

  @ManyToOne
  private Post post;



  @ManyToMany
  Set<Customer> likes;



  @OneToMany(mappedBy = "comment", cascade = {CascadeType.REMOVE})
  @ToString.Exclude
  @LazyCollection(LazyCollectionOption.EXTRA) // commentList.size(); 함수가 실행될 때 SELECT COUNT 실행
  private List<Answer> answers = new ArrayList<>();
}