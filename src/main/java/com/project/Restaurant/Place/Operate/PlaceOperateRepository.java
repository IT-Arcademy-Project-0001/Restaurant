package com.project.Restaurant.Place.Operate;

import com.project.Restaurant.Place.Owner.PlaceOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlaceOperateRepository extends JpaRepository<PlaceOperate, Integer> {
  List<PlaceOperate> findByPlaceOwner_Id(Long PlaceOwnerId);
  List<PlaceOperate>  findByPlaceOwner(PlaceOwner placeOwner);
}