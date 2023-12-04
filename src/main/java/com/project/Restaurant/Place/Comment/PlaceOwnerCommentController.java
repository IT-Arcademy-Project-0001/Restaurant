package com.project.Restaurant.Place.Comment;


import com.project.Restaurant.Member.consumer.Customer;
import com.project.Restaurant.Member.consumer.CustomerService;
import com.project.Restaurant.Reservation.Reservation;
import com.project.Restaurant.Reservation.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RequestMapping("/place")
@Controller
@RequiredArgsConstructor
public class PlaceOwnerCommentController {

  private final CustomerService customerService;
  private final ReservationService reservationService;
  private final PlaceOwnerCommentService placeOwnerCommentService;

  @GetMapping("/comment/{reservationId}")
  public String placecomment(Model model, @PathVariable("reservationId") Long reservationId,
                             PlaceOwnerCommentForm placeOwnerCommentForm) {

      Reservation reservation = this.reservationService.findById(reservationId);

      model.addAttribute("reservation", reservation);

    return "PlaceSearch/place_review";
  }

    @PostMapping("/comment/create")
    public String placecomment(Model model, @RequestParam("reservationId") Long reservationId, Principal principal,
                               @Valid PlaceOwnerCommentForm placeOwnerCommentForm, BindingResult bindingResult) {

      Reservation reservation = this.reservationService.findById(reservationId);
      Customer customer = customerService.findByusername(principal.getName());

      if (bindingResult.hasErrors()) {
        return "PlaceSearch/place_review";
      }

      this.placeOwnerCommentService.create(reservation.getPlaceOwner(), customer, placeOwnerCommentForm.getReviewSubject(), placeOwnerCommentForm.getReviewContent());

      return "redirect:/reservation/customerList";
    }
  }
