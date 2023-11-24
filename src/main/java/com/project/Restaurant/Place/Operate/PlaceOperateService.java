package com.project.Restaurant.Place.Operate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceOperateService {

  private final PlaceOperateRepository placeOperateRepository;
  public void saveoperate(List<OperateDto> operateDtoList){
    for (OperateDto operateDto : operateDtoList) {
      PlaceOperate placeOperate = new PlaceOperate();
      placeOperate.setPlaceOwner(null);
      placeOperate.setDay(operateDto.getDay());
      placeOperate.setOpenTime(operateDto.getOpenTime());
      placeOperate.setCloseTime(operateDto.getCloseTime());
      this.placeOperateRepository.save(placeOperate);
    }
  }

  public List<PlaceOperate>  getAllOperateList(Integer PlaceOwnerId){
    return this.placeOperateRepository.findByPlaceOwner_Id(PlaceOwnerId);
  }
}
