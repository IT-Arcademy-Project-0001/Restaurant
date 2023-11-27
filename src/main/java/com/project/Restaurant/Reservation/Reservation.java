package com.project.Restaurant.Reservation;


import com.project.Restaurant.Member.consumer.Customer;
import com.project.Restaurant.Member.owner.Owner;
import com.project.Restaurant.Place.Owner.PlaceOwner;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Reservation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDateTime ReservationTime;

  private String status;

  @ManyToOne
  private Customer customer;

  @ManyToOne
  private PlaceOwner placeOwner;

}
