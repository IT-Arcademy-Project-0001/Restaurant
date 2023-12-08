package com.project.Restaurant.Place.Owner.Tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceTagRepository extends JpaRepository<PlaceTag, Long> {
    List<PlaceTag> findByPlaceOwner_Id(Long id);
}
