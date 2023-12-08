package com.project.Restaurant.Place.Owner.Tag;

import com.project.Restaurant.Place.Owner.PlaceOwner;
import com.project.Restaurant.Place.Owner.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceTagService {
    private final PlaceService placeService;
    private final PlaceTagRepository placeTagRepository;
    public void saveTag(String tag, Long id){
        PlaceOwner placeOwner = this.placeService.findById(id);

        PlaceTag placeTag = new PlaceTag();
        placeTag.setTag(tag);
        placeTag.setPlaceOwner(placeOwner);

        this.placeTagRepository.save(placeTag);
    }

    public List<PlaceTag> findTags(Long id){
        List<PlaceTag> tagList = this.placeTagRepository.findByPlaceOwner_Id(id);
        return tagList;
    }
}
