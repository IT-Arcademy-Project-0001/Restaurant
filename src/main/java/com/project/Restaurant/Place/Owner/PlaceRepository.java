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
  Page<PlaceOwner> findByOwnerId(Long ownerId, Pageable pageable);

//  @Query("Select p From PlaceOwner p Where p.store Like %:kw%")

//  @Query("select "
//          + "distinct p "
//          + "from PlaceOwner p "
//          + "where "
//          + "   (p.id = :id) "
//          + "   and ( "
//          + "   p.store like %:kw% "
//          + "   )")

  @Query("Select p From PlaceOwner p Where p.owner.id = :id and p.store Like %:kw%")
  Page<PlaceOwner> findAllByKeyword(@Param("kw") String kw, @Param("id") Long OwnerId, Pageable pageable);

}
