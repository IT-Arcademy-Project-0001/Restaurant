package com.project.Restaurant.Place.Comment;

import com.project.Restaurant.Member.consumer.Customer;
import com.project.Restaurant.Place.Owner.PlaceOwner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PlaceOwnerCommentService {

  private final PlaceOwnerCommentRepository placeOwnerCommentRepository;

  public void create(PlaceOwner placeOwner, Customer customer, String subject, String content) {
    PlaceOwnerComment poc = new PlaceOwnerComment();
    poc.setPlaceOwner(placeOwner);
    poc.setSubject(subject);
    poc.setContent(content);
    poc.setLocalDateTime(LocalDateTime.now());
    poc.setCustomer(customer);
    this.placeOwnerCommentRepository.save(poc);
  }
}
