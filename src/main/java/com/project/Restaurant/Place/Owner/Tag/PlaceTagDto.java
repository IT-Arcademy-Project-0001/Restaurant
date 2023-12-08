package com.project.Restaurant.Place.Owner.Tag;

import com.project.Restaurant.Place.Owner.PlaceOwner;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;
@Getter
@Setter
@Component
public class PlaceTagDto {
    private Long id;
    private String tag;
    private List<PlaceTagDto> placeTagDtoList;
}
