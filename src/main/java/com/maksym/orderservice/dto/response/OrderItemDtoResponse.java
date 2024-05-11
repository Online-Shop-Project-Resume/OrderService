package com.maksym.orderservice.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemDtoResponse {

    private Long id;

    private String skuCode;

    private Integer quantity;

    private String inventoryId;

    private OrderDtoResponse order;
}
