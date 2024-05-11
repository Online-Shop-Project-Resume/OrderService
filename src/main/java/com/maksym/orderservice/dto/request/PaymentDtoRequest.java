package com.maksym.orderservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDtoRequest {

    @Positive(message = "Total must be a positive number")
    @NotNull(message = "Total cannot be null")
    private BigDecimal total;

    @Positive(message = "Order Id must be a positive number")
    @NotNull(message = "Order Id cannot be null")
    private Long orderId;

    @Positive(message = "Quantity must be a positive number")
    @NotNull(message = "Quantity cannot be null")
    private Integer quantity;
}
