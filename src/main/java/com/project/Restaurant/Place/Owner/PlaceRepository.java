package com.project.Restaurant.Place.Owner;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<PlaceOwner, Long> {

  PlaceOwner findById(Integer id);

}
