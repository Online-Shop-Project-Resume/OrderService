package com.maksym.orderservice.staticObject;

import com.maksym.orderservice.dto.request.OrderItemDtoRequest;
import com.maksym.orderservice.dto.response.OrderItemDtoResponse;
import com.maksym.orderservice.model.OrderItem;


public class StaticOrderItem {

    public static final Long ID = 1L;

    public static OrderItem orderItem1() {
        OrderItem model = new OrderItem();
        model.setId(ID);
        model.setSkuCode("skuCode");
        model.setQuantity(1);
        model.setInventoryId("inventoryId");
        model.setOrder(StaticOrder.order1());
        return model;
    }

    public static OrderItem orderItem2() {
        OrderItem model = new OrderItem();
        model.setId(ID);
        model.setSkuCode("skuCode");
        model.setQuantity(2);
        model.setInventoryId("inventoryId");
        model.setOrder(StaticOrder.order2());
        return model;
    }

    public static OrderItemDtoRequest orderItemDtoRequest1() {
        OrderItemDtoRequest dtoRequest = new OrderItemDtoRequest();
        dtoRequest.setSkuCode("skuCode");
        dtoRequest.setQuantity(1);
        dtoRequest.setInventoryId("inventoryId");
        dtoRequest.setOrderId(1L);
        return dtoRequest;
    }

    public static OrderItemDtoResponse orderItemDtoResponse1() {
        OrderItemDtoResponse dtoResponse = new OrderItemDtoResponse();
        dtoResponse.setId(ID);
        dtoResponse.setSkuCode("skuCode");
        dtoResponse.setQuantity(1);
        dtoResponse.setInventoryId("inventoryId");
        dtoResponse.setOrder(StaticOrder.orderDtoResponse1());
        return dtoResponse;
    }

    public static OrderItemDtoResponse orderItemDtoResponse2() {
        OrderItemDtoResponse dtoResponse = new OrderItemDtoResponse();
        dtoResponse.setId(ID);
        dtoResponse.setSkuCode("skuCode");
        dtoResponse.setQuantity(2);
        dtoResponse.setInventoryId("inventoryId");
        dtoResponse.setOrder(StaticOrder.orderDtoResponse1());
        return dtoResponse;
    }
}
