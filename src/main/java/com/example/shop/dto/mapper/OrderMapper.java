package com.example.shop.dto.mapper;

import com.example.shop.dto.OrderDTO;
import com.example.shop.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderDetailMapper.class})
public interface OrderMapper {

    @Mapping(source = "orderDetails", target = "orderDetailDTOS")
    @Mapping(source = "user.id", target = "userId")
    OrderDTO toDto(Order order);
    Order toOrder(OrderDTO dto);

    List<OrderDTO> toDtoList(List<Order> orders);
    List<Order> toOrderList(List<OrderDTO> dtos);
}
