package com.maksym.orderservice.dtoMapper;

import com.maksym.orderservice.dto.OrderItemRequest;
import com.maksym.orderservice.dto.OrderItemResponse;
import com.maksym.orderservice.model.Order;
import com.maksym.orderservice.model.OrderItem;

public class OrderItemMapper {
    public static OrderItemResponse toResponse(OrderItem orderItem) {
        OrderItemResponse orderItemResponse = new OrderItemResponse();
        orderItemResponse.setId(orderItem.getId());
        orderItemResponse.setQuantity(orderItem.getQuantity());
        orderItemResponse.setSkuCode(orderItem.getSkuCode());
        orderItemResponse.setInventoryId(orderItem.getInventoryId());
        orderItemResponse.setOrder(orderItem.getOrder());

        return orderItemResponse;
    }

    public static OrderItem toModel(OrderItemRequest orderItemRequest) {
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(orderItemRequest.getQuantity());
        orderItem.setSkuCode(orderItemRequest.getSkuCode());
        orderItem.setInventoryId(orderItemRequest.getInventoryId());
        if(orderItemRequest.getOrderId()!=null) {
            Order order = new Order();
            order.setId(orderItemRequest.getOrderId());
            orderItem.setOrder(order);
        }
        return orderItem;
    }

}
