package com.project.Restaurant.PlaceSearch;

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
  public String detail(Model model,  @PathVariable("categoryId") Integer categoryId, @PathVariable("id") Integer id) {

     // 카테고리아이디(CategoryId) 해당하는 가게 카테고리 범위 내에서 가게번호(Id)의 정보를 가져오고 해당 엔티티 객체를 model로 받는다)
    // 이후 타임리프 문법을 이용해 HTML 상세페이지에서 활용
    // 향후 서비스&레포지토리로 검색하는 것까지 구현하면 존재하지 않는 카테고리id나, 장소id는 에러 페이지가 뜰 것임

    return "PlaceSearch/place_info";
  }
}
