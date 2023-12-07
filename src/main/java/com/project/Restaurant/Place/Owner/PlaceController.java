package com.project.Restaurant.Place.Owner;

import com.project.Restaurant.Member.owner.Owner;
import com.project.Restaurant.Member.owner.OwnerService;
import com.project.Restaurant.Place.Operate.OperateDto;
import com.project.Restaurant.Place.Operate.PlaceOperate;
import com.project.Restaurant.Place.Operate.PlaceOperateService;
import com.project.Restaurant.Place.Owner.Tag.PlaceTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RequestMapping("/place/map")
@Controller
public class PlaceController {

  private final PlaceOperateService placeOperateService;
  private final PlaceService placeService;
  private final PlaceTagService placeTagService;
  private final OwnerService ownerService;
  @GetMapping("/regist")
  public String regist(Model model) {
    List<PlaceOperate> placeOperateList = this.placeOperateService.getAllOperateList(null);
    model.addAttribute("placeOperateList", placeOperateList);
    model.addAttribute("placeOwner", null);
    return "Place/PlaceRegist";
  }
  @PostMapping("/regist/info/save")
  public String infoSave(Model model, Principal principal, @RequestParam String storeName, @RequestParam String address, @RequestParam String detailAddress, @RequestParam String phoneNum, @RequestParam String category,@RequestParam Double latitude, @RequestParam Double longitude ){

    Owner owner = this.ownerService.findByusername(principal.getName());

    PlaceOwner placeOwner = this.placeService.savePlace(storeName,address,detailAddress,phoneNum,category,latitude,longitude,owner);
    model.addAttribute("placeOwner", placeOwner);

    List<PlaceOperate> placeOperateList = this.placeOperateService.getAllOperateList(placeOwner.getId());
    model.addAttribute("placeOperateList", placeOperateList);

    return "redirect:/place/map/regist/info/" + placeOwner.getId();
  }

  @PostMapping("/regist/info/subSave")
  public  String saveTag(@RequestParam String webSite, @RequestParam String storeMemo, @RequestParam Long placeId){

    this.placeService.saveSubInfo(webSite,storeMemo,placeId);

    return "redirect:/place/map/regist/info/" + placeId;
  }

  @PostMapping("/regist/info/tag")
  @ResponseBody   // json형태를 java객체로 쓰겠다는 의미
  public Map<String, String> saveTag(@RequestBody Map<String, Object> requestMap){

    List<String> tagList = (List<String>) requestMap.get("tagsArray");
    String placeOwnerId = (String) requestMap.get("placeOwnerId");

    for(String tag : tagList){
      this.placeTagService.saveTag(tag,(Long.valueOf(placeOwnerId)));
    }

    Map<String, String> response = new HashMap<>();
    response.put("redirectUrl", "/place/map/regist/info/" + Long.valueOf(placeOwnerId));
    return response;
  }

  @GetMapping("regist/list")
  public String getList(Principal principal,Model model){
    Owner owner = this.ownerService.findByusername(principal.getName());
    List<PlaceOwner> placeList = this.placeService.getPlaceOwnersByOwnerId(owner.getId());
    model.addAttribute("placeList",placeList);
    return "Place/PlaceRegistList";
  }
}
