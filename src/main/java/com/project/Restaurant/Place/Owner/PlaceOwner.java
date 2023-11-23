package com.project.Restaurant.Place.Owner;

import com.project.Restaurant.Member.owner.Owner;
import com.project.Restaurant.Place.Operate.PlaceOperate;
import com.project.Restaurant.Reservation.Reservation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
public class PlaceOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String store;           // 매장 이름
    private String storeImg;       // 매장 이미지
    private String storeAddress;    // 매장 위치 (도로명 주소 또는 지번주소 )

    @Column(nullable = false)
    private Double latitude;          // 위도

    @Column(nullable = false)
    private Double longitude;         // 경도

    private String callNum;           // 전화번호
    //    private String time;           // 매장 영업시간  >> PlaceOperate 테이블 생성
    private String link;            // 매장 웹사이트
    private String menu;            // 매장 메뉴
    private String menuImg;        // 메뉴 이미지
    private String post;            // 매장 소식 ( ex. Take out 10% 할인 진행합니다 )
    private String tag;             // 매장 태그 ( ex. #떡볶이맛집, #배달가능 )

    @Column(nullable = false)
    private String storeCategory;  // 매장 카테고리 ( ex. 음식점, 카페, 술집, )

    private String storeMemo;      // 추가 안내사항 작성

    @OneToMany(mappedBy = "placeOwner")
    private List<PlaceOperate> placeOperateList;

    @ManyToOne
    private Owner owner;

    @OneToMany(mappedBy="placeOwner", cascade = CascadeType.REMOVE)
    private List<Reservation> reservationList;
}