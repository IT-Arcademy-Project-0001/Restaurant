package com.project.Restaurant.Place.Comment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceOwnerCommentForm {

  @NotBlank(message = "후기 제목은 필수입니다.")
  private String reviewSubject;

  @NotBlank(message = "후기 내용은 필수입니다.")
  private String reviewContent;

}
