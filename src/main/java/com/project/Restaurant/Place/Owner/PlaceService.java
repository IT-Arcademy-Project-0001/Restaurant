package com.project.Restaurant.Place.Owner;

import com.project.Restaurant.Member.owner.Owner;
import com.project.Restaurant.Member.owner.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {
  private final PlaceRepository placeRepository;
  public PlaceOwner savePlace(String storeName, String address, String detailAddress, String phoneNum, String category, Double latitude, Double longitude,Owner owner){
    PlaceOwner placeOwner = new PlaceOwner();
    placeOwner.setStore(storeName);
    placeOwner.setStoreAddress(address + '/' + detailAddress);
    placeOwner.setCallNum(phoneNum);
    placeOwner.setStoreCategory(category);
    placeOwner.setLatitude(latitude);
    placeOwner.setLongitude(longitude);
    placeOwner.setOpeningDate(LocalDateTime.now());
    placeOwner.setOwner(owner);

    return this.placeRepository.save(placeOwner);
  }

  public PlaceOwner findById(Long id){
    return this.placeRepository.findById(id).get();
  }

  public void saveSubInfo(String webSite, String storeMemo, Long id){
    PlaceOwner placeOwner = this.placeRepository.findById(id).get();

    placeOwner.setLink(webSite);
    placeOwner.setStoreMemo(storeMemo);

    this.placeRepository.save(placeOwner);
  }

  public List<PlaceOwner> getPlaceOwnersByOwnerId(Long ownerId) {
    return this.placeRepository.findByOwnerId(ownerId); 
  }

  public Page<PlaceOwner> getList(int page, Long Id , String kw) {
    List<Sort.Order> sorts = new ArrayList<>();
    sorts.add(Sort.Order.desc("openingDate"));

    Pageable pageable = PageRequest.of(page, 5, Sort.by(sorts));

    return this.placeRepository.findAllByKeyword(kw, Id, pageable);
  }
 
}
