package com.maksym.orderservice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentRequest {
    private BigDecimal total;
    private Long orderId;
    private Integer quantity;
}
