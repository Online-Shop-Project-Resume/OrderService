package com.maksym.orderservice.staticObject;

import com.maksym.orderservice.dto.OrderRequest;
import com.maksym.orderservice.dto.OrderResponse;
import com.maksym.orderservice.model.Order;
import com.maksym.orderservice.util.enums.OrderStatus;

import java.time.LocalDateTime;

public class StaticOrder {
    public static Order order1(){
        Order order = new Order();
        order.setOrderNumber("1");
        order.setOrderDate(LocalDateTime.now());
        order.setId(1L);
        order.setUserId("1");
        order.setInventoryId("1");
        order.setStatus(OrderStatus.DELIVERED);
        return order;
    }
    public static OrderResponse orderResponse1(){
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderDate(LocalDateTime.now());
        orderResponse.setOrderNumber("1");
        orderResponse.setStatus(OrderStatus.DELIVERED);
        orderResponse.setUserId("1");
        orderResponse.setId(1L);
        return orderResponse;
    }

    public static OrderRequest orderRequest(){
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderNumber("1");
        orderRequest.setStatus(OrderStatus.DELIVERED);
        orderRequest.setUserId("1");
        return orderRequest;
    }
}
