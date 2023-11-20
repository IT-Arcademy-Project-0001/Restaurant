package com.project.Restaurant.PlaceSearch;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceSearchForm {

  private String keyword;

  private String pstarting;

  @NotEmpty(message = "출발지 선택은 필수사항입니다")
  private String pname1;

  private String paddress1;

  private String latclick1;

  private String lngclick1;

}
