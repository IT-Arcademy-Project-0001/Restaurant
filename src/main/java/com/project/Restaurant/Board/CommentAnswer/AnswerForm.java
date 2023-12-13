package com.project.Restaurant.Board.CommentAnswer;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AnswerForm {

    private Long id;
    @NotBlank(message = "내용은 필수항목입니다.")
    private String answerContents;
    private Long postId;
    private Long commentId;
    private Boolean secret = false;
    private Long parentId;
    private Long answerWriter;
}
