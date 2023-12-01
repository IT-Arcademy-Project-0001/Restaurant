package com.project.Restaurant.Reservation;

import com.project.Restaurant.Member.consumer.Customer;
import com.project.Restaurant.Member.consumer.CustomerService;
import com.project.Restaurant.Member.owner.Owner;
import com.project.Restaurant.Member.owner.OwnerService;
import com.project.Restaurant.Place.Customer.PlaceCustService;
import com.project.Restaurant.Place.Owner.PlaceOwner;
import com.project.Restaurant.Place.Owner.PlaceService;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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


        PlaceOwner placeOwner = this.placeService.findById(placeOwnerId);;
        Customer customer = this.customerService.findByusername(principal.getName());

        Reservation reservation = new Reservation();

        reservation.setReservationTime(LocalDateTime.now());
        reservation.setStatus("0");
        reservation.setCustomer(customer);
        reservation.setPlaceOwner(placeOwner);

        reservationService.save(reservation);

        return "redirect:/reservation/list";
    }

    @GetMapping("/reservation/ownerList")
    public String ownerList(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/member/login";
        }
        String loggedInUsername = principal.getName();
        Owner owner = ownerService.findByusername(loggedInUsername);

        List<Reservation> reservationOwner = reservationService.findByPlaceOwner(owner.getId());
        model.addAttribute("reservationOwnerList", reservationOwner);
        return "Reservation/owner_list";
    }

    @GetMapping("/reservation/customerList")
    public String customerList(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/member/login";
        }

        String loggedInUsername = principal.getName(); // 현재 로그인한 사용자의 ID

        // 현재 로그인한 사용자의 ID와 일치하는 예약 목록만 가져오기
        List<Reservation> reservationsList = this.reservationService.findByCustomerUsername(loggedInUsername);
        model.addAttribute("reservationList", reservationsList);

        return "Reservation/customer_list";
    }

    @PostMapping("/reservation/accept")
    public String acceptReservation(Model model,
                                    @RequestParam("reservationid") Long reservationId
    ) {

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



        private boolean isCustomerLoggedIn (Principal principal){
            if (principal != null && principal instanceof Authentication) {
                Authentication authentication = (Authentication) principal;
                return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
            }
            return false;
        }

        private boolean isOwnerLoggedIn (Principal principal){
            if (principal != null && principal instanceof Authentication) {
                Authentication authentication = (Authentication) principal;
                return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OWNER"));
            }
            return false;
        }
    }
