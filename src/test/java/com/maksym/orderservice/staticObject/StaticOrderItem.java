package com.maksym.orderservice.staticObject;

import com.maksym.orderservice.dto.OrderItemRequest;
import com.maksym.orderservice.dto.OrderItemResponse;
import com.maksym.orderservice.model.Order;
import com.maksym.orderservice.model.OrderItem;

public class StaticOrderItem {
    public static OrderItemResponse orderItemResponse(){
        OrderItemResponse orderItemResponse = new OrderItemResponse();
        orderItemResponse.setId(1L);
        orderItemResponse.setInventoryId("1");
        orderItemResponse.setQuantity(10);
        orderItemResponse.setSkuCode("SKU001");
        orderItemResponse.setOrder(StaticOrder.order1());
        return orderItemResponse;
    }

    public static OrderItem orderItem(){
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setInventoryId("1");
        orderItem.setQuantity(10);
        orderItem.setSkuCode("SKU001");
        orderItem.setOrder(StaticOrder.order1());
        return orderItem;
    }

    public static OrderItemRequest orderItemRequest(){
        OrderItemRequest orderItemRequest = new OrderItemRequest();
        orderItemRequest.setInventoryId("1");
        orderItemRequest.setQuantity(10);
        orderItemRequest.setSkuCode("SKU001");
        orderItemRequest.setOrderId(1L);
        return orderItemRequest;
    }
}
