package com.project.Restaurant.Place.Operate;

import com.project.Restaurant.Place.Menu.PlaceMenu;
import com.project.Restaurant.Place.Menu.PlaceMenuService;
import com.project.Restaurant.Place.Owner.PlaceOwner;
import com.project.Restaurant.Place.Owner.PlaceOwnerDto;
import com.project.Restaurant.Place.Owner.PlaceService;
import com.project.Restaurant.Place.Owner.Tag.PlaceTag;
import com.project.Restaurant.Place.Owner.Tag.PlaceTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/place/map")
@Controller
public class PlaceOperateController {

    private final PlaceOperateService placeOperateService;
    private final PlaceMenuService placeMenuService;
    private final PlaceService placeService;
    private final PlaceTagService placeTagService;

    @GetMapping("/regist/owner")
    public String regist2() {
        return "Place/PlaceRegistOpentime";
    }

    @PostMapping("/regist/owner/time")
    public String saveOperateTime(@ModelAttribute OperateDto operateDto, @RequestParam Long placeOwnerId){
        List<OperateDto> operateDtoList = operateDto.getOperateDtoList();
        this.placeOperateService.saveOperate(operateDtoList, placeOwnerId);
        return "redirect:/place/map/regist/info/" + placeOwnerId;
    }

    @PostMapping("/regist/owner/time/update")
    public String updateOperateTime(@ModelAttribute OperateDto operateDto,@RequestParam Long placeOwnerId){
        List<OperateDto> operateDtoList = operateDto.getOperateDtoList();
        this.placeOperateService.updateOperate(operateDtoList,placeOwnerId);
        return "redirect:/place/map/regist/info/" + placeOwnerId;
    }
    @GetMapping("/regist/info/{id}")
    public String infosave2(Model model, @PathVariable Long id ){
        PlaceOwner placeOwner = this.placeService.findById(id);
        PlaceOwnerDto placeOwnerDto = placeOwner.convertDto();
        model.addAttribute("placeOwner", placeOwnerDto);

        Long ownerId = placeOwner.getId();
        List<OperateDto> operateDtoList = placeOperateService.getAllOperateDtoList(ownerId);
        model.addAttribute("placeOperateList", operateDtoList);

        List<PlaceMenu> placeMenuList  =  this.placeMenuService.findByPlaceOwnerId(id);
        model.addAttribute("menus", placeMenuList);

        List<PlaceTag> tagList = this.placeTagService.findTags(id);
        model.addAttribute("TagList",tagList);

//        List<PlaceOperate> placeOperateList = this.placeOperateService.getAllOperateList(Math.toIntExact(placeOwner.getId()));
//        model.addAttribute("placeOperateList", placeOperateList);

        return "Place/PlaceRegist";
    }
    @GetMapping("/regist/info/detail/{id}")
    public String infosave3(Model model, @PathVariable Long id ){
        PlaceOwner placeOwner = this.placeService.findById(id);
        PlaceOwnerDto placeOwnerDto = placeOwner.convertDto();
        model.addAttribute("placeOwner", placeOwnerDto);

        Long ownerId = placeOwner.getId();
        List<OperateDto> operateDtoList = placeOperateService.getAllOperateDtoList(ownerId);
        model.addAttribute("placeOperateList", operateDtoList);

        List<PlaceMenu> placeMenuList  =  this.placeMenuService.findByPlaceOwnerId(id);
        model.addAttribute("menus", placeMenuList);

        List<PlaceTag> tagList = this.placeTagService.findTags(id);
        model.addAttribute("TagList",tagList);

//        List<PlaceOperate> placeOperateList = this.placeOperateService.getAllOperateList(Math.toIntExact(placeOwner.getId()));
//        model.addAttribute("placeOperateList", placeOperateList);

        return "Place/PlaceRegistDetailList";
    }

}
