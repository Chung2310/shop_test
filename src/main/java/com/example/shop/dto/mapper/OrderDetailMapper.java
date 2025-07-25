package com.example.shop.dto.mapper;

import com.example.shop.dto.OrderDetailDTO;
import com.example.shop.model.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface OrderDetailMapper {

    @Mapping(source = "book", target = "bookDTO")
    OrderDetailDTO toDto(OrderDetail orderDetail);

    List<OrderDetailDTO> toDtoList(List<OrderDetail> orderDetails);

    OrderDetail toEntity(OrderDetailDTO dto);

    List<OrderDetail> toEntityList(List<OrderDetailDTO> dtoList);
}
