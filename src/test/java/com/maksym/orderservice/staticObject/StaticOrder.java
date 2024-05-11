package com.maksym.orderservice.staticObject;

import com.maksym.orderservice.dto.request.OrderDtoRequest;
import com.maksym.orderservice.dto.response.OrderDtoResponse;
import com.maksym.orderservice.model.Order;

import java.time.LocalDateTime;

public class StaticOrder {

    public static final Long ID = 1L;

    public static Order order1() {
        Order model = new Order();
        model.setId(ID);
        model.setOrderNumber("orderNumber");
        model.setOrderDate(LocalDateTime.MIN);
        model.setInventoryId("inventoryId");
        model.setStatus(StaticOrderStatus.orderStatus1());
        model.setUserId("userId");
        return model;
    }

    public static Order order2() {
        Order model = new Order();
        model.setId(ID);
        model.setOrderNumber("orderNumber");
        model.setOrderDate(LocalDateTime.MIN);
        model.setInventoryId("inventoryId");
        model.setStatus(StaticOrderStatus.orderStatus2());
        model.setUserId("userId");
        return model;
    }

    public static OrderDtoRequest orderDtoRequest1() {
        OrderDtoRequest dtoRequest = new OrderDtoRequest();
        dtoRequest.setOrderNumber("orderNumber");
        dtoRequest.setInventoryId("inventoryId");
        dtoRequest.setStatus("status");
        dtoRequest.setUserId("userId");
        return dtoRequest;
    }

    public static OrderDtoResponse orderDtoResponse1() {
        OrderDtoResponse dtoResponse = new OrderDtoResponse();
        dtoResponse.setId(ID);
        dtoResponse.setOrderNumber("orderNumber");
        dtoResponse.setOrderDate(LocalDateTime.MIN);
        dtoResponse.setInventoryId("inventoryId");
        dtoResponse.setStatus(StaticOrderStatus.orderStatusDtoResponse1());
        dtoResponse.setUserId("userId");
        return dtoResponse;
    }

    public static OrderDtoResponse orderDtoResponse2() {
        OrderDtoResponse dtoResponse = new OrderDtoResponse();
        dtoResponse.setId(ID);
        dtoResponse.setOrderNumber("orderNumber");
        dtoResponse.setOrderDate(LocalDateTime.MIN);
        dtoResponse.setInventoryId("inventoryId");
        dtoResponse.setStatus(StaticOrderStatus.orderStatusDtoResponse1());
        dtoResponse.setUserId("userId");
        return dtoResponse;
    }
}
