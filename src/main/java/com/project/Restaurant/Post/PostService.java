package com.project.Restaurant.Post;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    public Page<Post> getList(int page){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("localDateTime"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return postRepository.findAll(pageable);
    }

    public Post getPost(Long id){
        Optional<Post> postOptional = this.postRepository.findById(id);
        if(postOptional.isPresent()){
            return postOptional.get();
        }else{
            throw new IllegalArgumentException("없는 게시물입니다.");
        }
    }

    public void create(String title, String content) {
        Post p = new Post();
        p.setTitle(title);
        p.setContent(content);
        p.setLocalDateTime(LocalDateTime.now());
        this.postRepository.save(p);
    }
}
