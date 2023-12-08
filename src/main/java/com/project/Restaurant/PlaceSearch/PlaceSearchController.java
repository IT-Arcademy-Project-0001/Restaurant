package com.project.Restaurant.PlaceSearch;

import com.project.Restaurant.Place.Comment.PlaceOwnerComment;
import com.project.Restaurant.Place.Menu.PlaceMenu;
import com.project.Restaurant.Place.Menu.PlaceMenuService;
import com.project.Restaurant.Place.Operate.PlaceOperate;
import com.project.Restaurant.Place.Operate.PlaceOperateService;
import com.project.Restaurant.Place.Owner.PlaceOwner;
import com.project.Restaurant.Place.Owner.Tag.PlaceTag;
import com.project.Restaurant.Place.Owner.Tag.PlaceTagService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/place")
public class PlaceSearchController {

  private final PlaceSearchService placeSearchService;
  private final PlaceTagService placeTagService;
  private final PlaceMenuService placeMenuService;
  private final PlaceOperateService placeOperateService;

  // @GetMapping(value = "/search", produces = "text/html")의 의미가 내재되어 있다(기본값으로 작용).
  @GetMapping("/search")
  public String targetSearch(Model model) {

    return "PlaceSearch/place_search";
  }

  @GetMapping(value = "/search", produces = "application/json")
  @ResponseBody
  public List<PlaceSearch> targetSearchJson(Model model, @RequestParam("latitude") Double latitude, @RequestParam("longitude") Double longitude,
                                            @RequestParam(value = "order[]", required = false) List<Integer> order) {

    List<PlaceSearch> searchResult = this.placeSearchService.searchPlace(latitude, longitude, order);

    model.addAttribute("placeList", searchResult);

    return searchResult;
  }

  @GetMapping(value = "/searchModal", produces = "application/json")
  @ResponseBody
  public List<PlaceSearch> targetSearchModalJson(Model model, @RequestParam("latitude2") Double latitude, @RequestParam("longitude2") Double longitude,
                                            @RequestParam(value = "order2[]", required = false) List<Integer> order) {

    List<PlaceSearch> searchResult2 = this.placeSearchService.searchPlace(latitude, longitude, order);

    model.addAttribute("placeModalList", searchResult2);

    return searchResult2;
  }

//  @PostMapping("/search")
//  public String targetSearch(Model model, @RequestParam Map<String, String> requestParams,
//                             @Valid PlaceSearchForm placeSearchForm, BindingResult bindingResult) {
//
//    if (bindingResult.hasErrors()) {
//      return "redirect:/PlaceSearch/place_search";
//    }
//
//    return "redirect:/PlaceSearch/place_search";
//  }


  @GetMapping("/{categoryId}/{id}")
  public String detail(Model model,  @PathVariable("categoryId") Long categoryId, @PathVariable("id") Long id) {

    PlaceOwner placeOwner = this.placeSearchService.getPlace(id);
    model.addAttribute("placeOwner", placeOwner);

    List<PlaceTag> placeTagList = this.placeTagService.findTags(id);
    model.addAttribute("placeTags", placeTagList);

    List<PlaceMenu> placeMenuList = this.placeMenuService.findByPlaceOwnerId(id);
    model.addAttribute("placeMenu", placeMenuList);

    PlaceMenu placeMenuThumbnail = this.placeMenuService.findByPlaceOwnerId(id).get(0);
    model.addAttribute("placeMenuThumbnail", placeMenuThumbnail);

    List<PlaceOperate> placeOperateList = this.placeOperateService.getAllOperateList(id);
    model.addAttribute("placeOperate", placeOperateList);

    return "PlaceSearch/place_info";
  }
}
