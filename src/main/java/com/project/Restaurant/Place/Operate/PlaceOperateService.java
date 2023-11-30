package com.project.Restaurant.Place.Operate;

import com.project.Restaurant.Place.Owner.PlaceOwner;
import com.project.Restaurant.Place.Owner.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PlaceOperateService {
  private final PlaceOperateRepository placeOperateRepository;
  private final PlaceService placeService;
  public void saveOperate(List<OperateDto> operateDtoList, Long id){
    PlaceOwner placeOwner = this.placeService.findById(id);
//        List<PlaceOperate> savedOperates = new ArrayList<>();

    for (OperateDto operateDto : operateDtoList) {
      PlaceOperate placeOperate = new PlaceOperate();
      placeOperate.setPlaceOwner(placeOwner);
      placeOperate.setDay(operateDto.getDay());
      placeOperate.setOpenTime(operateDto.getOpenTime());
      placeOperate.setCloseTime(operateDto.getCloseTime());
      this.placeOperateRepository.save(placeOperate);
//            savedOperates.add(this.placeOperateRepository.save(placeOperate));
    }

//        return savedOperates;
  }

  public void updateOperate(List<OperateDto> placeOperateList, Long id){
    PlaceOwner placeOwner = this.placeService.findById(id);
    List<PlaceOperate> placeOperate  = placeOwner.getPlaceOperateList();
    int i = 0;

    for (OperateDto operateDto : placeOperateList) {
      if (!placeOperate.isEmpty()) {
        PlaceOperate existingOperate = placeOperate.get(i);
        existingOperate.setDay(operateDto.getDay());
        existingOperate.setOpenTime(operateDto.getOpenTime());
        existingOperate.setCloseTime(operateDto.getCloseTime());
        this.placeOperateRepository.save(existingOperate);
        i++;
      } else {
        PlaceOperate newOperate = new PlaceOperate();
        newOperate.setPlaceOwner(placeOwner);
        newOperate.setDay(operateDto.getDay());
        newOperate.setOpenTime(operateDto.getOpenTime());
        newOperate.setCloseTime(operateDto.getCloseTime());
        this.placeOperateRepository.save(newOperate);
      }
    }
  }


  public List<PlaceOperate>  getAllOperateList(Long PlaceOwnerId){
    return this.placeOperateRepository.findByPlaceOwner_Id(PlaceOwnerId);
  }

  public List<OperateDto>  getAllOperateDtoList(Long PlaceOwnerId){
    List<PlaceOperate> placeOperateList = this.placeOperateRepository.findByPlaceOwner_Id(PlaceOwnerId);
    List<OperateDto> operateDtoList = new ArrayList<>();
    for (PlaceOperate operate : placeOperateList) {
      operateDtoList.add(operate.convertDto());
    }

    return operateDtoList;
  }

}
