package com.project.Restaurant.Board.CommentAnswer;

import com.project.Restaurant.Board.Post.Post;
import com.project.Restaurant.Board.PostComment.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    int countByPost(Post post);

    int countByComment(Comment comment);

    Page<Answer> findAllByPost(Post post, Pageable pageable);

    Page<Answer> findAllByComment(Comment comment, Pageable pageable);
}
