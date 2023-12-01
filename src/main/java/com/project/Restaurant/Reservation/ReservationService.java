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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final PlaceService placeService;
    private final OwnerService ownerService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);



    public List<Reservation> findAll() {

        return reservationRepository.findAll();
    }

    public Reservation findById(Long id) {

        return reservationRepository.findById(id).get();
    }

    public Reservation save(Reservation reservation) {

        return reservationRepository.save(reservation);
    }

    public List<Reservation> findByCustomerUsername(String username) {
        return reservationRepository.findByCustomerUsername(username);
    }

    public List<Reservation> findByPlaceOwnerId(Long id) {
        return reservationRepository.findByPlaceOwner_Id(id);
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

    @Transactional
    public void reservationCancel(Long reservationId) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
        if(optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            if("0".equals(reservation.getStatus())) {
                reservation.setStatus("3");
                reservationRepository.save(reservation);
                scheduler.schedule(() -> deleteCancelledReservation(reservationId), 30, TimeUnit.SECONDS);
            }
        }

    }
    private void deleteCancelledReservation(Long reservationId) {
        // 실제로 리스트에서 예약 삭제하는 코드 추가
        reservationRepository.deleteById(reservationId);
    }
}

