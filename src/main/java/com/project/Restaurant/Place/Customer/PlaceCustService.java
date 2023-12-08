package com.project.Restaurant.Place.Customer;

import com.project.Restaurant.Member.consumer.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.plaf.PanelUI;

@RequiredArgsConstructor
@Service
public class PlaceCustService {

    private final PlaceCustRepository placeCustRepository;
    public void addnewplace(String name, String locationAddress, String detailedaddress, String category, Double locationLat, Double locationLng,
                            String memo, Customer customer){

        PlaceCustomer placeCustomer = new PlaceCustomer();

        placeCustomer.setStore(name);
        placeCustomer.setStoreAddress(locationAddress);
        placeCustomer.setStoreDetailedAddress(detailedaddress);
        placeCustomer.setStoreCategory(category);
        placeCustomer.setLatitude(locationLat);
        placeCustomer.setLongitude(locationLng);
        placeCustomer.setStoreMemo(memo);
        placeCustomer.setCustomer(customer);

        this.placeCustRepository.save(placeCustomer);
    }

    public boolean checkPlaceExists(String storeAddress, String storeDetailedAddress, Double latitude, Double longitude) {

        return placeCustRepository.existsByStoreAddressAndStoreDetailedAddressAndLatitudeAndLongitude(
                storeAddress, storeDetailedAddress, latitude, longitude
        );
    }
}
