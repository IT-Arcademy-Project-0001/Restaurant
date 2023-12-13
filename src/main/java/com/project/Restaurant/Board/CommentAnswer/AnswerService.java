package com.project.Restaurant.Board.CommentAnswer;


import com.project.Restaurant.Board.Post.Post;
import com.project.Restaurant.Board.PostComment.Comment;
import com.project.Restaurant.Member.consumer.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {

    private final AnswerRepository answerRepository;


    @Transactional
    public Answer createByComment(String content, Boolean secret, Customer customer, Comment comment){
        Answer newAnswer = Answer.builder()
        		.content(content)
                .customer(customer)
                .comment(comment)
                .secret(secret)
                .build();

                answerRepository.save(newAnswer);

                return newAnswer;
    }

    public int getLastPageNumberByComment(Comment comment){
        int answerCount = answerRepository.countByComment(comment);
        int pageSize = 10;
        int lastPageNumber = (int)Math.ceil((double) answerCount / pageSize);
        // 스프링 0페이지 부터 시작하니 1 빼주기
        // 단 댓글이 1개도 없던 상황일 경우, 댓글이 달린 직후에는 0일테니 -1하면 음수값이 나온다.
        // 따라서 음수이면 0을 반환하도록 수정
        if (lastPageNumber - 1 < 0)
            return 0;
        else
            return lastPageNumber - 1;
    }

    public Answer getAnswer(Long answerId){
        return answerRepository.findById(answerId).orElse(null);
    }

    @Transactional
    public Answer createReplyAnswerByComment(String answerContents, Boolean secret, Customer customer, Comment comment,
                                              Answer parent) {
        Answer newAnswer = Answer.builder()
                .content(answerContents)
                .customer(customer)
                .comment(comment)
                .secret(secret)
                .parent(parent)
                .build();
        // 대댓글 저장
        answerRepository.save(newAnswer);
        return newAnswer;
    }


    public Page<Answer> getAnswerPageByComment(int page, Comment comment) {
        Pageable pageable = PageRequest.of(page, 5); // 페이지네이션 정보
        return answerRepository.findAllByComment(comment, pageable);
    }

    @Transactional
    public void modify(Answer answer, String content, Boolean secret) {
        Answer mAnswer = answer.toBuilder()
                .content(content)
                .secret(secret)
                .build();
        answerRepository.save(mAnswer);
    }

    @Transactional
    public void delete(Answer answer) {
        if (answer == null) {
            throw new IllegalArgumentException("answer cannot be null");
        }
        if (answer.getChildren().size() != 0) {
            // 자식이 있으면 삭제 상태만 변경
            answer.deleteParent();
        } else { // 자식이 없다 -> 대댓글이 없다 -> 객체 그냥 삭제해도 된다.
            // 삭제 가능한 조상 댓글을 구해서 삭제
            // ex) 할아버지 - 아버지 - 대댓글, 3자라 했을 때 대댓글 입장에서 자식이 없으니 삭제 가능
            // => 삭제하면 아버지도 삭제 가능 => 할아버지도 삭제 가능하니 이런식으로 조상 찾기 메서드
            Answer tmp = getDeletableAncestorAnswer(answer);
            answerRepository.delete(tmp);
        }
    }


    @Transactional
    public Answer getDeletableAncestorAnswer(Answer answer) {

        Answer parent = answer.getParent();
        if(parent != null && parent.getChildren().size() == 1 && parent.isDeleted() == true){
            // 부모가 있고, 부모의 자식이 1개(지금 삭제하는 댓글)이고, 부모의 삭제 상태가 TRUE인 댓글이라면 재귀
            // 삭제가능 댓글 -> 만일 댓글의 조상(대댓글의 입장에서 할아버지 댓글)도 해당 댓글 삭제 시 삭제 가능한지 확인
            // 삭제 -> Cascade 옵션으로 가장 부모만 삭제 해도 자식들도 다 삭제 가능

            // Ajax로 비동기로 리스트 가져오기에, 대댓글 1개인거 삭제할 때 연관관계 삭제하고 부모 댓글 삭제하기 필요
            // 컨트롤러가 아닌 서비스의 삭제에서 처리해주는 이유는 연관관계를 삭제해주면 parent를 구할 수 없기에 여기서 끊어줘야 함
            // 연관관계만 끊어주면 orphanRemoval 옵션으로 자식 객체는 삭제되니 부모를 삭제 대상으로 넘기면 됨
            parent.getChildren().remove(answer);

            return getDeletableAncestorAnswer(parent);
        }
        return answer;
    }

    public int getPageNumberByComment(Comment comment, Answer answer, int pageSize) {
        // 자식 댓글이면, 부모 댓글이 원래 있던 페이지 번호를 구해야 하므로 변경
        if(answer.getParent() != null) {
            answer = answer.getParent();
        }
        List<Answer> answersList = comment.getAnswers();
        int index = answersList.indexOf(answer);

        if (index == -1) {
            throw new IllegalArgumentException("해당 댓글이 존재하지 않습니다.");
        }

        return index / pageSize;
    }


}



