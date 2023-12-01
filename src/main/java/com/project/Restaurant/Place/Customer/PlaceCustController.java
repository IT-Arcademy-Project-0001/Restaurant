package com.project.Restaurant.Place.Customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("/place")
@Controller
public class PlaceCustController {

    private final PlaceCustService placeCustService;

    private final PlaceCustRepository placeCustRepository;

    @GetMapping("/map")
    public String map() {
        return "Place/Map";
    }

    @PostMapping("/map/add")
    public String customeradd(Model model, @RequestParam String placename, @RequestParam String locationaddress, @RequestParam String locationdetailedaddress
            , @RequestParam String category, @RequestParam Double locationlat, @RequestParam Double locationlng, @RequestParam String memo)  // 매장 이름, 매장 주소, 매장 상세주소, 매장 카테고리, 위도, 경도, 메모
    {
        boolean isPlaceExists = placeCustService.checkPlaceExists(locationaddress, locationdetailedaddress, locationlat, locationlng);

        if(!isPlaceExists){
            this.placeCustService.addnewplace(placename,locationaddress,locationdetailedaddress,category,locationlat,locationlng,memo);
            model.addAttribute("message", "등록되었습니다.");
        } else {
            model.addAttribute("message", "이미 등록된 위치입니다.");
        }

        return "redirect:/place/Map";
    }


}
