package com.maksym.orderservice.dto;

import com.maksym.orderservice.model.Order;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemRequest {
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
    private String inventoryId;
    private Long orderId;
}
