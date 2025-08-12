package com.example.shop.dto.mapper;

import com.example.shop.dto.ProductDTO;
import com.example.shop.model.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    // ánh xạ 1 đối tượng
    ProductDTO toDto(Product product);
    Product toEntity(ProductDTO dto);

    // ánh xạ danh sách
    List<ProductDTO> toDtoList(List<Product> products);
    List<Product> toEntityList(List<ProductDTO> dtos);
}
