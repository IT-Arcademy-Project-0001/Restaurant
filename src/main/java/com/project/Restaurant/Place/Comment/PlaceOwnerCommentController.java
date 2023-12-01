package com.project.Restaurant.Place.Comment;


import com.project.Restaurant.Reservation.Reservation;
import com.project.Restaurant.Reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/place")
@Controller
@RequiredArgsConstructor
public class PlaceOwnerCommentController {

  private final ReservationService reservationService;

  @GetMapping("/comment/{reservationId}")
  public String placecomment(Model model, @PathVariable("reservationId") Long reservationId) {

      Reservation reservation = this.reservationService.findById(reservationId);

      model.addAttribute("reservation", reservation);

    return "PlaceSearch/place_review";
  }
}
