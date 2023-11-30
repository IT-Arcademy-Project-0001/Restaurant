package com.project.Restaurant.Place.Menu;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceMenuRepository extends JpaRepository<PlaceMenu, Integer> {
    List<PlaceMenu> findByPlaceOwner_Id(Long ownerId);
}
