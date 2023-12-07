package com.project.Restaurant.Board.Post;

import lombok.Getter;


@Getter
public enum PostEnum {
    NOTICE(0),
    FREE(1),
    REQUEST(2);

    private int status;

    PostEnum(int status){
        this.status = status;
    }
}
