package com.project.Restaurant.Reservation;

import com.project.Restaurant.Member.owner.Owner;
import com.project.Restaurant.Place.Owner.PlaceOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {


    List<Reservation> findByCustomerUsername(String username);


    List<Reservation> findByPlaceOwner_Id(Long id);

}








