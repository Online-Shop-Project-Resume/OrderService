package com.maksym.orderservice.dto;

import com.maksym.orderservice.model.Order;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemRequest {
    @NotBlank
    private String skuCode;
    @Positive
    private Integer quantity;
    @NotNull
    private String inventoryId;
    @NotNull
    private Long orderId;
}
