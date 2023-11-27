package com.project.Restaurant.Place.Comment;

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
public class PlaceOwnerComment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 200)
  private String content;
  private LocalDateTime localDateTime;

  @ManyToOne
  private Customer customer;

  @ManyToOne
  private PlaceOwner placeOwner;
}
