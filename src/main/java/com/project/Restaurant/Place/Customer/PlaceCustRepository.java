package com.project.Restaurant.Place.Customer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceCustRepository extends JpaRepository<PlaceCustomer, Long> {
//    PlaceCustomer  findBystoreAddressAndstoreDetailedAddress(String storeAddress, String storeDetailedAddress);
boolean existsByStoreAddressAndStoreDetailedAddressAndLatitudeAndLongitude(
        String storeAddress, String storeDetailedAddress, Double latitude, Double longitude
);
}
