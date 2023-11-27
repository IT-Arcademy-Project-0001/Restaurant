package com.project.Restaurant.PlaceSearch;


import jakarta.validation.Valid;
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
  public String targetSearch(Model model, PlaceSearchForm placeSearchForm) {

    return "PlaceSearch/place_search";
  }

  @GetMapping(value = "/search", produces = "application/json")
  @ResponseBody
  public List<PlaceSearch> targetSearchJson(@RequestParam("latitude") Double latitude, @RequestParam("longitude") Double longitude) {

    List<PlaceSearch> searchResult = this.placeSearchService.searchPlace(latitude, longitude);

    return searchResult;
  }

  @PostMapping("/search")
  public String targetSearch(Model model, @RequestParam Map<String, String> requestParams,
                             @Valid PlaceSearchForm placeSearchForm, BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      return "redirect:/PlaceSearch/place_search";
    }


    Double targetLat = Double.valueOf(placeSearchForm.getLatclick1());
    Double targetLng = Double.valueOf(placeSearchForm.getLngclick1());

//    List<PlaceSearch> searchResult = this.placeSearchService.searchPlace(targetLat, targetLng);

//    model.addAttribute("searchresult", searchResult);


    return "redirect:/PlaceSearch/place_search";

  }
}
