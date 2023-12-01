package com.project.Restaurant.PlaceSearch;

import com.project.Restaurant.Place.Owner.PlaceOwner;
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

  // @GetMapping(value = "/search", produces = "text/html")의 의미가 내재되어 있다(기본값으로 작용).
  @GetMapping("/search")
  public String targetSearch(Model model) {

    return "PlaceSearch/place_search";
  }

  @GetMapping(value = "/search", produces = "application/json")
  @ResponseBody
  public List<PlaceSearch> targetSearchJson(@RequestParam("latitude") Double latitude, @RequestParam("longitude") Double longitude,
                                            @RequestParam(value = "order[]", required = false) List<Integer> order) {

    List<PlaceSearch> searchResult = this.placeSearchService.searchPlace(latitude, longitude, order);

    return searchResult;
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

    return "PlaceSearch/place_info";
  }
}
