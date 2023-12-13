package com.project.Restaurant.Place.Owner.Tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaceTagRepository extends JpaRepository<PlaceTag, Long> {
    List<PlaceTag> findByPlaceOwner_Id(Long id);
    Optional<PlaceTag> findById(Long id);
}
