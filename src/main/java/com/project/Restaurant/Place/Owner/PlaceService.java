package com.project.Restaurant.Place.Owner;

import com.project.Restaurant.Member.owner.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {
  private final PlaceRepository placeRepository;
  public PlaceOwner savePlace(String storeName, String address, String detailAddress, String phoneNum, String category, Double latitude, Double longitude ){
    PlaceOwner placeOwner = new PlaceOwner();
    placeOwner.setStore(storeName);
    placeOwner.setStoreAddress(address + '/' + detailAddress);
    placeOwner.setCallNum(phoneNum);
    placeOwner.setStoreCategory(category);
    placeOwner.setLatitude(latitude);
    placeOwner.setLongitude(longitude);
    return this.placeRepository.save(placeOwner);
  }

  public PlaceOwner findById(Long id){
    return this.placeRepository.findById(id).get();
  }

  public List<PlaceOwner> getPlaceOwnersByOwnerId(Long ownerId) {
    return placeRepository.findByOwnerId(ownerId);
  }

}
