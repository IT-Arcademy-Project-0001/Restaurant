package com.project.Restaurant.Reservation;

import com.project.Restaurant.Member.owner.OwnerService;
import com.project.Restaurant.Place.Owner.PlaceOwner;
import com.project.Restaurant.Place.Owner.PlaceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final PlaceService placeService;
    private final OwnerService ownerService;


    public List<Reservation> findAll() {

        return reservationRepository.findAll();
    }

    public Reservation save(Reservation reservation) {

        return reservationRepository.save(reservation);
    }

    public List<Reservation> findByCustomerUsername(String username) {
        return reservationRepository.findByCustomerUsername(username);
    }

    public List<Reservation> findByPlaceOwner(Long id) {
        return reservationRepository.findByPlaceOwnerId(id);
    }

    @Transactional
    public void acceptReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new IllegalArgumentException("해당 예약이 존재하지 않습니다."));

        reservation.setStatus("1");
        reservationRepository.save(reservation);
    }

    @Transactional
    public void completeReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new IllegalArgumentException("해당 예약이 존재하지 않습니다."));
        reservation.setStatus("2");
        reservationRepository.save(reservation);
    }




}
