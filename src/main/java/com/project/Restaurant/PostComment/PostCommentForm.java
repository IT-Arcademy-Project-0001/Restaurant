package com.project.Restaurant.PostComment;


import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCommentForm {


    @NotEmpty(message = "내용입력")
    private String content;
}
