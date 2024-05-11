package com.maksym.orderservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDtoResponse {

    private Long id;

    private BigDecimal total;

    private Long orderId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")

    private LocalDateTime paymentDate;

    private Integer quantity;
}
