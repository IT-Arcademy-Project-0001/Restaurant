package com.project.Restaurant.Board.CommentAnswer;


import com.project.Restaurant.Board.Post.Post;
import com.project.Restaurant.Board.PostComment.Comment;
import com.project.Restaurant.Member.consumer.Customer;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Post post;

    @ManyToOne
    private Comment comment;

    @ToString.Exclude
    @ManyToOne
    private Answer parent;

    // Cascade REMOVE 불가 : 자식 댓글이 있는 상태에서, 그냥 댓글 삭제하면 자식 댓글 전부 지워짐
    // OrphanRemoval로 대댓글과 연관관계 끊어지면 삭제되게 설정
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default // 빌더패턴 리스트시 초기화
    private List<Answer> children = new ArrayList<>();

    private String content;

    @CreatedDate
    private LocalDateTime localDateTime;
    @LastModifiedDate
    private LocalDateTime modifyDate;

    @Builder.Default
    private Boolean secret = false;

    // 삭제 여부 나타내는 속성 추가
    @Builder.Default
    private Boolean deleted = false;

    @PrePersist
    public void prePersist() {
        this.modifyDate = null;
    }

    public void deleteParent() {
        deleted = true;
    }

    // 타임리프에서 비밀 댓글이면 댓글의 내용이 안보이게 하기 위함
    public boolean isSecret() {
        return this.secret == true;
    }

    // 타임리프에서 삭제 댓글이면 댓글의 내용이 안보이게 하기 위함
    public boolean isDeleted() {
        return this.deleted == true;
    }
}
