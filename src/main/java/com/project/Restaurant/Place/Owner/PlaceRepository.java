package com.project.Restaurant.Place.Owner;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<PlaceOwner, Long> {

  List<PlaceOwner> findByOwnerId(Long ownerId);

}
