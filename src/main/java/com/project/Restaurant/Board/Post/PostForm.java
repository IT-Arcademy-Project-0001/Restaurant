package com.project.Restaurant.Board.Post;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostForm {

    @NotEmpty
    @Size(max=200)
    private String title;

    @NotEmpty
    private String content;
}
