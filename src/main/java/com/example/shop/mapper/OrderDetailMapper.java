package com.example.shop.mapper;

import com.example.shop.model.order.OrderDetailDTO;
import com.example.shop.model.order.OrderDetail;
import com.example.shop.model.order.OrderDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderDetailMapper {

    @Mapping(source = "product", target = "productDTO")
    OrderDetailDTO toDto(OrderDetail orderDetail);

    List<OrderDetailDTO> toDtoList(List<OrderDetail> orderDetails);

    OrderDetail toEntity(OrderDetailDTO dto);

    List<OrderDetail> toEntityList(List<OrderDetailDTO> dtoList);

    @Mapping(source = "product", target = "productResponse")
    OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail);
}
