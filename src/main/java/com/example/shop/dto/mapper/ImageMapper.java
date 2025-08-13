package com.example.shop.dto.mapper;

import com.example.shop.dto.ImageDTO;
import com.example.shop.model.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageDTO toDTO(Image image);
    Image toEntity(ImageDTO imageDTO);

    List<ImageDTO> toDTOs(List<Image> images);
    List<Image> toEntities(List<ImageDTO> imageDTOS);
}
