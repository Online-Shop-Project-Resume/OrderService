package com.maksym.orderservice.dtoMapper;

import com.maksym.orderservice.dto.OrderRequest;
import com.maksym.orderservice.dto.OrderResponse;
import com.maksym.orderservice.model.Order;

import java.time.LocalDateTime;

public class OrderMapper {
    public static Order toModel(OrderRequest orderRequest){
        if (orderRequest == null) {
            return null;
        }
        Order order = new Order();
        order.setOrderNumber(orderRequest.getOrderNumber());
        order.setOrderDate(LocalDateTime.now());
        order.setOrderNumber(orderRequest.getOrderNumber());
        order.setStatus(orderRequest.getStatus());
        order.setUserId(orderRequest.getUserId());
        return order;
    }
    public static OrderResponse toResponse(Order order) {
        if (order == null) {
            return null;
        }
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setOrderNumber(order.getOrderNumber());
        orderResponse.setOrderDate(order.getOrderDate());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setUserId(order.getUserId());
        orderResponse.setOrderItemList(order.getOrderItemList());
        return orderResponse;
    }

}
