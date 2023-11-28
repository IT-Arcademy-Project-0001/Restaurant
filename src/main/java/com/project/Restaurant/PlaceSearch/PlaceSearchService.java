package com.project.Restaurant.PlaceSearch;

import com.project.Restaurant.DataNotFoundException;
import com.project.Restaurant.Place.Customer.PlaceCustRepository;
import com.project.Restaurant.Place.Customer.PlaceCustomer;
import com.project.Restaurant.Place.Owner.PlaceOwner;
import com.project.Restaurant.Place.Owner.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PlaceSearchService {

  private final PlaceCustRepository placeCustRepository;
  private final PlaceRepository placeOwnerRepository;

  public PlaceCustomer getPlace(Long id) {
    // JpaRepository의 기능을 이용하여 기사번호 id에 맞는 기사를 찾아 가져오는 것임
    Optional<PlaceCustomer> placeCustomer = this.placeCustRepository.findById(id);
    if (placeCustomer.isPresent()) {
      return placeCustomer.get();
    } else {
      throw new DataNotFoundException("article not found");
    }
  }

  public List<PlaceSearch> searchPlace(Double currentLat, Double currentLng, Integer order) {

    List<PlaceCustomer> placeResults1 = placeCustRepository.findAll();
    List<PlaceOwner> placeResults2 = placeOwnerRepository.findAll();

    // 하버사인 공식에 의해 현재 보고 있는 맵중심을 기준으로 반경 2km 이내의 장소만 출력
    // 카카오 기준으로는 30km 이내만 도보 길안내를 알려줌. (5km 성인기준 1시간 15분 정도 소요)
    double radius = 30;


    List<PlaceSearch> searchResults = new ArrayList<>();

    if (order == 2){
      for (PlaceCustomer place : placeResults1) {

        double distance = calculateDistance(currentLat, currentLng, place.getLatitude(), place.getLongitude());

        PlaceSearch ps = new PlaceSearch();

        if (distance < radius) {
          ps.setStore(place.getStore());
          ps.setLocationLat(String.valueOf(place.getLatitude()));
          ps.setLocationLng(String.valueOf(place.getLongitude()));
          // ... 나머지 필드 설정
          searchResults.add(ps);
        }
      }
    } else if (order == 3) {

      for (PlaceOwner place2 : placeResults2) {

        double distance = calculateDistance(currentLat, currentLng, place2.getLatitude(), place2.getLongitude());

        PlaceSearch psf = new PlaceSearch();

        if (distance < radius) {
          psf.setStore(place2.getStore());
          psf.setLocationLat(String.valueOf(place2.getLatitude()));
          psf.setLocationLng(String.valueOf(place2.getLongitude()));
          // ... 나머지 필드 설정
          searchResults.add(psf);
        }
      }
    }
    return searchResults;
  }

  private double calculateDistance(double aLat, double aLng, double bLat, double bLng) {

    double earthRadius = 6371; // 지구 반지름 (단위: km)
    double dLat = Math.toRadians(bLat - aLat);
    double dLng = Math.toRadians(bLng - aLng);
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
            + Math.cos(Math.toRadians(aLat)) * Math.cos(Math.toRadians(bLat))
            * Math.sin(dLng / 2) * Math.sin(dLng / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return earthRadius * c;

  }
}
