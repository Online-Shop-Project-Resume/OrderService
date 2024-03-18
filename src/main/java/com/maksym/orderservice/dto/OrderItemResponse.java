package com.maksym.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.maksym.orderservice.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemResponse {
    private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
    private String inventoryId;
    @JsonIgnoreProperties("orderItemList")
    private Order order;
}
