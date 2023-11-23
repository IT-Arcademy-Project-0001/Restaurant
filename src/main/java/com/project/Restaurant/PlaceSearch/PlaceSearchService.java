package com.project.Restaurant.PlaceSearch;

//import com.project.Restaurant.DataNotFoundException;
//import com.project.Restaurant.Place.Place;
//import com.project.Restaurant.Place.PlaceRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@RequiredArgsConstructor
//@Service
//public class PlaceSearchService {
//
//  private final PlaceRepository placeRepository;
//
////  private final FavoriteRepository favoriteRepository;
//
//  public Place getPlace(Long id) {
//    // JpaRepository의 기능을 이용하여 기사번호 id에 맞는 기사를 찾아 가져오는 것임
//    Optional<Place> place = this.placeRepository.findById(id);
//    if (place.isPresent()) {
//      return place.get();
//    } else {
//      throw new DataNotFoundException("article not found");
//    }
//  }
//
//  public List<PlaceSearch> searchPlace(Double latclick1, Double lngclick1) {
//
//    List<Place> placeResults = placeRepository.findAll();
////    List<Favorite> favoriteResults = favoriteRepository.findAll();
//
//    // 하버사인 공식에 의해 출발지를 기준으로 반경 5km 이내의 장소만 출력 (5km 성인기준 1시간 15분 정도 소요)
//    Integer radius = 5;
//
//    List<PlaceSearch> searchResults = new ArrayList<>();
//
//    for (Place place : placeResults) {
//      PlaceSearch ps = new PlaceSearch();
//      ps.setStore(place.getStore());
//      ps.setStoreAddress(place.getStoreAddress());
//      // ... 나머지 필드 설정
//
//      searchResults.add(ps);
//    }
//
////    for (Favorite favorite : favorite2Results) {
////      PlaceSearch psf = new PlaceSearch();
////      psf.setStore(favorite.getStore());
////      psf.setStoreAddress(favorite.getStoreAddress());
////      // ... 나머지 필드 설정
////
////      searchResults.add(psf);
////    }
//
//    return searchResults;
//  }
//
//  private double calculateDistance(double aLat, double aLng, double bLat, double bLng) {
//
//    double earthRadius = 6371; // 지구 반지름 (단위: km)
//    double dLat = Math.toRadians(bLat - aLat);
//    double dLng = Math.toRadians(bLng - aLng);
//    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
//            + Math.cos(Math.toRadians(aLat)) * Math.cos(Math.toRadians(bLat))
//            * Math.sin(dLng / 2) * Math.sin(dLng / 2);
//    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//    return earthRadius * c;
//  }
//}
