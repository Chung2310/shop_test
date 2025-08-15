package com.example.shop.mapper;

import com.example.shop.model.product.ProductDTO;
import com.example.shop.model.product.Product;
import com.example.shop.model.product.ProductRequest;
import com.example.shop.model.product.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    // ánh xạ 1 đối tượng
    ProductDTO toDto(Product product);
    Product toEntity(ProductDTO dto);

    // ánh xạ danh sách
    List<ProductDTO> toDtoList(List<Product> products);
    List<Product> toEntityList(List<ProductDTO> dtos);

    ProductResponse toProductResponse(Product product);
    List<ProductResponse> toProductResponseList(List<Product> productList);

    Product toProductEntity(ProductRequest productRequest);
}
