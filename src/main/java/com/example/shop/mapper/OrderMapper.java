package com.example.shop.mapper;

import com.example.shop.model.order.OrderDTO;
import com.example.shop.model.order.Order;
import com.example.shop.model.order.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class,OrderDetailMapper.class})
public interface OrderMapper {

    @Mapping(source = "orderDetails", target = "orderDetailDTOS")
    @Mapping(source = "userEntity.id", target = "userId")
    OrderDTO toDto(Order order);
    Order toOrder(OrderDTO dto);

    List<OrderDTO> toDtoList(List<Order> orders);
    List<Order> toOrderList(List<OrderDTO> dtos);

    @Mapping(source = "userEntity", target = "userResponse")
    @Mapping(source = "orderDetails" , target = "orderDetailResponses")
    OrderResponse toOrderResponse(Order order);
    List<OrderResponse> toOrderResponseList(List<Order> orders);
}
