package com.maksym.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentResponse {
    private Long id;
    private BigDecimal total;
    private Long orderId;
    private LocalDateTime paymentDate;
    private Integer quantity;
}
