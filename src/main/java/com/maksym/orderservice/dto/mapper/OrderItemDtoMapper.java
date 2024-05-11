package com.maksym.orderservice.dto.mapper;

import com.maksym.orderservice.model.Order;
import com.maksym.orderservice.model.OrderItem;
import com.maksym.orderservice.dto.request.OrderItemDtoRequest;
import com.maksym.orderservice.dto.response.OrderItemDtoResponse;

public class OrderItemDtoMapper {

    public static OrderItem toModel(OrderItemDtoRequest request) {
        OrderItem model = new OrderItem();

        model.setSkuCode(request.getSkuCode());
        model.setQuantity(request.getQuantity());
        model.setInventoryId(request.getInventoryId());
        Order order = new Order();
        order.setId(request.getOrderId());
        model.setOrder(order);

        return model;
    }

    public static OrderItemDtoResponse toResponse(OrderItem model) {
        OrderItemDtoResponse response = new OrderItemDtoResponse();

        response.setId(model.getId());
        response.setSkuCode(model.getSkuCode());
        response.setQuantity(model.getQuantity());
        response.setInventoryId(model.getInventoryId());
        response.setOrder(OrderDtoMapper.toResponse(model.getOrder()));

        return response;
    }

    private OrderItemDtoMapper() {}

}
