package com.project.Restaurant.Place.Operate;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlaceOperateRepository extends JpaRepository<PlaceOperate, Integer> {
  List<PlaceOperate> findByPlaceOwner_Id(Integer PlaceOwnerId);
}