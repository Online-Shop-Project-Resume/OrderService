package com.maksym.orderservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentRequest {
    @Positive
    private BigDecimal total;
    @NotNull
    private Long orderId;
    @Positive
    private Integer quantity;
}
