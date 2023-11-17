package com.project.Restaurant.Post;

import com.project.Restaurant.Member.Member;
import com.project.Restaurant.PostComment.PostComment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false ,length = 30)
  private String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  private LocalDateTime localDateTime;

  @ManyToOne
  private Member author;

  @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
  private List<PostComment> postCommentList;
}
