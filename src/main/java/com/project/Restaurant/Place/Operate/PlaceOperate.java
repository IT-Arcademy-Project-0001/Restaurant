package com.project.Restaurant.Place.Operate;
import com.project.Restaurant.Place.Owner.PlaceOwner;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PlaceOperate {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String day;         // 요일

  private String openTime;    // 오픈시간

  private String closeTime;    // 마감시간

  @ManyToOne
  private PlaceOwner placeOwner;

  public OperateDto convertDto() {
    OperateDto operateDto = new OperateDto();
    operateDto.setDay(this.day);
    operateDto.setOpenTime(this.openTime);
    operateDto.setCloseTime(this.closeTime);

    return operateDto;
  }
}