package com.project.Restaurant.Board.CommentAnswer;


import com.project.Restaurant.Board.Post.Post;
import com.project.Restaurant.Board.PostComment.Comment;
import com.project.Restaurant.Member.consumer.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;


    public Answer createAnswer(Post post, Comment comment, String content, Customer customer) {
        Answer answer = new Answer();

        answer.setContent(content);
        answer.setLocalDateTime(LocalDateTime.now());
        answer.setPost(post);
        answer.setComment(comment);
        answer.setCustomer(customer);
        this.answerRepository.save(answer);
        return answer;
    }
}



