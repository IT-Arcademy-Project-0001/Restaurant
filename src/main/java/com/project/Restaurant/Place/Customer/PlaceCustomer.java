package com.project.Restaurant.Place.Customer;

import com.project.Restaurant.Member.consumer.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PlaceCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String store;           // 매장 이름

    private String storeAddress;    // 매장 위치 (도로명 주소 또는 지번주소 )

    private String storeDetailedAddress;    // 매장 상세주소

    @Column(nullable = false)
    private Double latitude;          // 위도

    @Column(nullable = false)
    private Double longitude;         // 경도

    @Column(nullable = false)
    private String storeCategory;  // 매장 카테고리 ( ex. 음식점, 카페, 술집, )

    private String storeMemo;      // 추가 안내사항 작성
}
