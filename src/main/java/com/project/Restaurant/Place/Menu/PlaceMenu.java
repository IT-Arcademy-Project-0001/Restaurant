package com.project.Restaurant.Place.Menu;

import com.project.Restaurant.Place.Owner.PlaceOwner;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PlaceMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String origFileName;   // 파일 원본명

    @Column(nullable = false)
    private String filePath;      // 파일 저장 경로

    private String menuName;      // 메뉴 이름

    private String menuPrice;       // 메뉴 가격

    @ManyToOne
    private PlaceOwner placeOwner;

}
