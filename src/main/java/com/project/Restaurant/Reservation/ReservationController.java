package com.project.Restaurant.Reservation;

import com.project.Restaurant.Member.consumer.Customer;
import com.project.Restaurant.Member.consumer.CustomerService;
import com.project.Restaurant.Member.owner.Owner;
import com.project.Restaurant.Member.owner.OwnerService;
import com.project.Restaurant.Place.Owner.PlaceOwner;
import com.project.Restaurant.Place.Owner.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final OwnerService ownerService;
    private final CustomerService customerService;
    private final PlaceService placeService;

    @PostMapping("/reservation/request")
    public String Request(@RequestParam("placeOwnerId") Long placeOwnerId, Principal principal) {


        PlaceOwner placeOwner = this.placeService.findById(placeOwnerId);
        ;
        Customer customer = this.customerService.findByusername(principal.getName());

        Reservation reservation = new Reservation();

        reservation.setReservationDate(LocalDateTime.now());
        reservation.setStatus("0");
        reservation.setCustomer(customer);
        reservation.setPlaceOwner(placeOwner);

        reservationService.save(reservation);

        return "redirect:/reservation/customerList";
    }

    @GetMapping("/reservation/ownerList")
    public String ownerList(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/member/login";
        }
        // 예약 리스트를 가져오는 순서...
        // 로그인한 사람의 정보를 가져오고.
        String loggedInUsername = principal.getName();
        Owner owner = ownerService.findByusername(loggedInUsername);
        // 그 사람이 소유하고 있는 건물 리스트를 가져오고. (한명의 사장이 복수의 건물을 소유할 수 있음)
        List<PlaceOwner> placeOwners = placeService.getPlaceOwnersByOwnerId(owner.getId());
        // 각 건물의 고유번호를 가지고 있는 예약 리스트를 담을 리스트
        List<Reservation> reservationOwnerList = new ArrayList<>();
        // 각 PlaceOwner에 대한 예약을 가져와서 reservationOwnerList에 추가
        for (PlaceOwner placeOwner : placeOwners) {
            List<Reservation> reservations = reservationService.findByPlaceOwnerId(placeOwner.getId());
            reservationOwnerList.addAll(reservations);
        }
        model.addAttribute("reservationOwnerList", reservationOwnerList);
        return "Reservation/owner_list";
    }

    @GetMapping("/reservation/customerList")
    public String customerList(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/member/login";
        }
        String loggedInUsername = principal.getName(); // 현재 로그인한 사용자의 ID
        List<Reservation> reservationsList = this.reservationService.findByCustomerUsername(loggedInUsername);
        model.addAttribute("reservationList", reservationsList);
        return "Reservation/customer_list";
    }

    @PostMapping("/reservation/accept")
    public String acceptReservation(Model model, @RequestParam("reservationid") Long reservationId) {
        // 예약 수락 비즈니스 로직 호출
        reservationService.acceptReservation(reservationId);
        // 세션에 상태 정보 저장 (예: "ACCEPTED")
        model.addAttribute("reservationStatus", "ACCEPTED");
        return "redirect:/reservation/ownerList";
    }

    @PostMapping("/reservation/complete")
    public String completeReservation(Model model, @RequestParam("reservationId") Long reservationId) {
        reservationService.completeReservation(reservationId);
        model.addAttribute("reservationStatus", "COMPLETE");
        return "redirect:/reservation/customerList";
    }

    @PostMapping("/reservation/cancel")
    public String reservationCancel(Model model, @RequestParam("reservationId") Long reservationId) {
        reservationService.reservationCancel(reservationId);
        model.addAttribute("reservationStatus", "CANCELED");
        return "redirect:/reservation/customerList";
    }

    @PostMapping("/reservation/change")
    public String changeReservationTime(@RequestParam("id") Long reservationId,
                                        @RequestParam("newReservationDate") @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime newReservationDate) {

        reservationService.changeReservationTime(reservationId, newReservationDate);
        return "redirect:/reservation/customerList";
    }

    private boolean isCustomerLoggedIn(Principal principal) {
        if (principal != null && principal instanceof Authentication) {
            Authentication authentication = (Authentication) principal;
            return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        }
        return false;
    }

    private boolean isOwnerLoggedIn(Principal principal) {
        if (principal != null && principal instanceof Authentication) {
            Authentication authentication = (Authentication) principal;
            return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OWNER"));
        }
        return false;
    }
}
