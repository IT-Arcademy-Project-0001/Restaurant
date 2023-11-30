package com.project.Restaurant.Place.Owner;

import com.project.Restaurant.Place.Operate.OperateDto;
import com.project.Restaurant.Place.Operate.PlaceOperate;
import com.project.Restaurant.Place.Operate.PlaceOperateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RequiredArgsConstructor
@RequestMapping("/place/map")
@Controller
public class PlaceController {

  private final PlaceOperateService placeOperateService;
  private final PlaceService placeService;

  @GetMapping("/regist")
  public String regist(Model model) {
    List<PlaceOperate> placeOperateList = this.placeOperateService.getAllOperateList(null);
    model.addAttribute("placeOperateList", placeOperateList);
    model.addAttribute("placeOwner", null);
    return "MapRegist";
  }
  @PostMapping("/regist/info/save")
  public String infoSave(Model model, @RequestParam String storeName, @RequestParam String address, @RequestParam String detailAddress, @RequestParam String phoneNum, @RequestParam String category,@RequestParam Double latitude, @RequestParam Double longitude ){
    PlaceOwner placeOwner = this.placeService.savePlace(storeName,address,detailAddress,phoneNum,category,latitude,longitude);
    model.addAttribute("placeOwner", placeOwner);

    List<PlaceOperate> placeOperateList = this.placeOperateService.getAllOperateList(placeOwner.getId());
    model.addAttribute("placeOperateList", placeOperateList);

    return "redirect:/place/map/regist/info/" + placeOwner.getId();
  }
}
