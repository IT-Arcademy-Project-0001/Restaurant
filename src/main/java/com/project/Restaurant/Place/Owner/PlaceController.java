package com.project.Restaurant.Place.Owner;

import com.project.Restaurant.Member.owner.Owner; 
import com.project.Restaurant.Member.owner.OwnerService;
import com.project.Restaurant.Place.Menu.PlaceMenu;
import com.project.Restaurant.Place.Menu.PlaceMenuService; 
import com.project.Restaurant.Place.Operate.OperateDto;
import com.project.Restaurant.Place.Operate.PlaceOperate;
import com.project.Restaurant.Place.Operate.PlaceOperateService;
import com.project.Restaurant.Place.Owner.Tag.PlaceTag;
import com.project.Restaurant.Place.Owner.Tag.PlaceTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
  private final PlaceMenuService placeMenuService;
  private final OperateDto operateDto; 
   
  @GetMapping("/regist")
  public String regist(Model model, @RequestParam(name = "parameter", required = false) String parameter) {
    List<PlaceOperate> placeOperateList = this.placeOperateService.getAllOperateList(null);
    model.addAttribute("placeOperateList", placeOperateList);
    model.addAttribute("placeOwner", null);
    model.addAttribute("parameter",2);
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
  public  String saveAddInfo(@RequestParam String webSite, @RequestParam String storeMemo, @RequestParam Long placeId){

    this.placeService.saveSubInfo(webSite,storeMemo,placeId);

    return "redirect:/place/map/regist/info/" + placeId;
  }

  @PostMapping("/regist/info/subUpdate")
  public  String updateAddInfo(@RequestParam String webSite, @RequestParam String storeMemo, @RequestParam Long placeId){

    this.placeService.saveSubInfo(webSite,storeMemo,placeId);

    return "redirect:/place/map/regist/list/detail/" + placeId;
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

//  @GetMapping("regist/list")
//  public String getList(Principal principal,Model model, @RequestParam(value="page", defaultValue="0") int page){
//    Owner owner = this.ownerService.findByusername(principal.getName());
//    Page<PlaceOwner> paging = this.placeService.getList(page,owner.getId());
//    model.addAttribute("paging",paging);
//
//    return "Place/PlaceRegistList";
//  }

  @GetMapping("regist/list")
  public String list(Principal principal, Model model, @RequestParam(value="page", defaultValue="0") int page,
                     @RequestParam(value = "kw", defaultValue = "") String kw) {
    Owner owner = this.ownerService.findByusername(principal.getName());
    Page<PlaceOwner> paging = this.placeService.getList(page, owner.getId(), kw);
    model.addAttribute("paging", paging);
    model.addAttribute("parameter", 1);
    return "Place/PlaceRegistList";
  }

  @GetMapping("regist/list/detail/{id}")
  public String getListDetail(Model model,@PathVariable("id") Long id){
    PlaceOwner placeOwner = this.placeService.findById(id);
    model.addAttribute("placeOwner", placeOwner);

    Long ownerId = placeOwner.getId();
    List<OperateDto> operateDtoList = placeOperateService.getAllOperateDtoList(ownerId);
    model.addAttribute("placeOperateList", operateDtoList);

    List<PlaceMenu> placeMenuList  =  this.placeMenuService.findByPlaceOwnerId(id);
    model.addAttribute("menus", placeMenuList);

    List<PlaceTag> tagList = this.placeTagService.findTags(id);
    model.addAttribute("TagList",tagList);

    model.addAttribute("parameter",1);

    return "Place/PlaceRegistDetailList";
  }

  @PostMapping("regist/list/delete/{tagId}")
  public String deleteTags(Model model,@PathVariable("tagId") Long tagId, @RequestParam Long POwnerId){
    this.placeTagService.deleteTag(tagId);
//
    return "redirect:/place/map/regist/list/detail/" + POwnerId;
  }


}
