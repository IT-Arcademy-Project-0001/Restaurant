package com.project.Restaurant.Place.Comment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceOwnerCommentForm {

  private String reservationId;

  @NotBlank(message = "별점을 선택해주세요.")
  private String starRate;

  @NotBlank(message = "후기 내용은 필수입니다.")
  private String reviewContent;

}
