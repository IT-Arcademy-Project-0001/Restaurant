package com.project.Restaurant.Reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


@Controller
public class ReservationController {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @PostMapping("/reservation/request")
    public String Request(@RequestParam String storeId,@RequestParam String customerId,@RequestParam String ownerId) {
        Reservation reservation = new Reservation();
        reservation.setStoreId(storeId);
        reservation.setCustomerId(customerId);



        reservation.setOwnerId(ownerId);
        reservation.setReservationTime(LocalDateTime.now());

        reservationRepository.save(reservation);

        return "redirect:/reservation/list";
    }

    @GetMapping("/reservation/request")
    public String Request() {
        return "reservation_request";
    }

    @GetMapping("/reservation/list")
    public String list(Model model) {
        List<Reservation> reservationsList = this.reservationRepository.findAll();
        model.addAttribute("reservationList", reservationsList);
        return "reservation_list";
    }
}
