package com.project.Restaurant.PlaceSearch;

import com.project.Restaurant.Place.Place;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/place")
public class PlaceSearchController {

  private final PlaceSearchService placeSearchService;

  @GetMapping("/search")
  public String targetSearch(Model model, PlaceSearchForm placeSearchForm) {
    return "PlaceSearch/place_search";
  }

  @PostMapping("/search")
  public String targetSearch(Model model, @RequestParam Map<String, String> requestParams,
                             @Valid PlaceSearchForm placeSearchForm, BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      return "PlaceSearch/place_search";
    }

    Double targetLat = Double.valueOf(placeSearchForm.getLatclick1());
    Double targetLng = Double.valueOf(placeSearchForm.getLngclick1());

    List<PlaceSearch> searchResult = this.placeSearchService.searchPlace(targetLat, targetLng);

    model.addAttribute("searchresult", searchResult);

    return "PlaceSearch/place_search";
  }
}
