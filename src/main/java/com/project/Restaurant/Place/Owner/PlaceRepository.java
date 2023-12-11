package com.project.Restaurant.Place.Owner;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlaceRepository extends JpaRepository<PlaceOwner, Long> {
  List<PlaceOwner> findByOwnerId(Long ownerId);

//  @Query("SELECT p FROM PlaceOwner p WHERE p.store LIKE %:kw%")
//  Page<PlaceOwner> findAllByKeyword(@Param("kw") String kw, Pageable pageable)
//Page<PlaceOwner> findAll(Pageable pageable);
//
//  @Query("select "
//          + "distinct p "
//          + "from PlaceOwner p "
//          + "where "
//          + "   p.store like %:kw% "
//          + "   )")
//  Page<PlaceOwner> findAllByKeyword(@Param("kw") String kw, Pageable pageable);



}
