package com.example.shop.mapper;

import com.example.shop.model.image.ImageDTO;
import com.example.shop.model.image.Image;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageDTO toDTO(Image image);
    Image toEntity(ImageDTO imageDTO);

    List<ImageDTO> toDTOs(List<Image> images);
    List<Image> toEntities(List<ImageDTO> imageDTOS);
}
