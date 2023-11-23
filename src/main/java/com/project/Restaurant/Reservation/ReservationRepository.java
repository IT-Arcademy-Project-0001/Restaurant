package com.project.Restaurant.Reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
//    List<Reservation> findByOwnerIdAndConfirmedIsFalse(String ownerId);
//
//    List<Reservation> findByOwnerIdAndConfirmedIsTrue(String ownerId);
}
//