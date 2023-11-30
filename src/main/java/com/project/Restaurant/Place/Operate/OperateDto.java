package com.project.Restaurant.Place.Operate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
public class OperateDto {
  private Long id;
  private String day;
  private String openTime;
  private String closeTime;
  private List<OperateDto> operateDtoList;

}
