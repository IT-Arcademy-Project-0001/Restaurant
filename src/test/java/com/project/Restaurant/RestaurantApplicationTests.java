package com.project.Restaurant;

import com.project.Restaurant.Board.Post.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RestaurantApplicationTests {


    @Autowired
    private PostService postService;

    @Test
    void testJpa() {

        for (int i = 1; i <= 1; i++) {
            String title = String.format("대전 찐맛집 입니다.");
            String content = "내용무";
            this.postService.create(title, content, null, 2);
        }
    }
}
