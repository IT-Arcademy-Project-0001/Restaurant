package com.project.Restaurant.PlaceSearch;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceSearch {

    private String store;           // 매장 이름
    private String locationLat;          // 위도
    private String locationLng;         // 경도

    private Integer categoryOrder;

}
