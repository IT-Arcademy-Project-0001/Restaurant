package com.project.Restaurant.Board.Post;


import com.project.Restaurant.DataNotFoundException;
import com.project.Restaurant.Member.consumer.Customer;
import com.project.Restaurant.Board.PostComment.Comment;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;


//    private Specification<Post> search(String kw) {
//        return new Specification<>() {
//            private static final long serialVersionUID = 1L;
//
//            @Override
//            public Predicate toPredicate(Root<Post> p, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                query.distinct(true);  // 중복을 제거
//                Join<Post, Customer> u1 = p.join("customer", JoinType.LEFT);
//                Join<Post, Comment> a = p.join("commentList", JoinType.LEFT);
//                Join<Comment, Customer> u2 = a.join("customer", JoinType.LEFT);
//                return cb.or(cb.like(p.get("title"), "%" + kw + "%"), // 제목
//                        cb.like(p.get("content"), "%" + kw + "%"),      // 내용
//                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
//                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
//                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
//            }
//        };
//    }


    public Page<Post> getList(int category, int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("localDateTime"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

//        Specification<Post> spec = search(kw);
        return postRepository.findAllByKeywordAndType(kw, category, pageable);
    }



    public Post getPost(Long id) {
        Optional<Post> optionalPost = this.postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post post1 = optionalPost.get();

            // 조회수가 null인 경우 0으로 초기화
            if (post1.getView() == null) {
                post1.setView(0L);
            }

            post1.setView(post1.getView() + 1);
            return this.postRepository.save(post1);
        } else {
            throw new DataNotFoundException("post not found");
        }
    }


    public void create(String title, String content, Customer customer, int category) {
        Post p = new Post();
        p.setTitle(title);
        p.setContent(content);
        p.setLocalDateTime(LocalDateTime.now());
        p.setCustomer(customer);
        p.setCategory(category);
        this.postRepository.save(p);
    }

    public void modify(Post post, String title, String content) {
        post.setTitle(title);
        post.setContent(content);
        post.setModifyDate(LocalDateTime.now());
        this.postRepository.save(post);
    }

    public void delete(Post post) {
        this.postRepository.delete(post);
    }

    public void likes(Post post, Customer customer) {
        post.getLikes().add(customer);
        this.postRepository.save(post);
    }
}
