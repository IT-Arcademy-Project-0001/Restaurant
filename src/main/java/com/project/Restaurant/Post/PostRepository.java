package com.project.Restaurant.Post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Post findByTitle(String title);
    Post findByTitleAndContent(String title, String content);
    List<Post> findByTitleLike(String title);
    Page<Post> findAll(Pageable pageable);

    Page<Post> findAll(Specification<Post> spec, Pageable pageable);

    @Query("select "
            + "distinct p "
            + "from Post p "
            + "left outer join Customer u1 on p.customer=u1 "
            + "left outer join PostComment a on a.post=p "
            + "left outer join Customer u2 on a.customer=u2 "
            + "where "
            + "   p.title like %:kw% "
            + "   or p.content like %:kw% "
            + "   or u1.username like %:kw% "
            + "   or a.content like %:kw% "
            + "   or u2.username like %:kw% ")
    Page<Post> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
}
