package com.maksym.orderservice.dto.mapper;

import com.maksym.orderservice.model.Order;
import com.maksym.orderservice.dto.request.OrderDtoRequest;
import com.maksym.orderservice.dto.response.OrderDtoResponse;
import com.maksym.orderservice.util.enums.OrderStatus;

public class OrderDtoMapper {

    public static Order toModel(OrderDtoRequest request) {
        Order model = new Order();

        model.setOrderNumber(request.getOrderNumber());
        model.setInventoryId(request.getInventoryId());
        model.setStatus(OrderStatus.valueOf(request.getStatus()));
        model.setUserId(request.getUserId());

        return model;
    }

    public static OrderDtoResponse toResponse(Order model) {
        OrderDtoResponse response = new OrderDtoResponse();

        response.setId(model.getId());
        response.setOrderNumber(model.getOrderNumber());
        response.setOrderDate(model.getOrderDate());
        response.setInventoryId(model.getInventoryId());
        response.setStatus(model.getStatus());
        response.setUserId(model.getUserId());

        return response;
    }

    private OrderDtoMapper() {}

}
