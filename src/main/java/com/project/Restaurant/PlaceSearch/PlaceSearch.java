package com.project.Restaurant.PlaceSearch;

import com.project.Restaurant.Place.Comment.PlaceOwnerComment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlaceSearch {

    private Long id;
    private String store;           // 매장 이름
    private String locationLat;          // 위도
    private String locationLng;         // 경도
    private Integer categoryOrder;
    private String address;
    private String category;
    private Long reviewCount;
    private String starRate;

}
