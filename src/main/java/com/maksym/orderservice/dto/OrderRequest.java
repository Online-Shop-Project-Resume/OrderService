package com.maksym.orderservice.dto;

import com.maksym.orderservice.util.enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderRequest {
    @NotBlank
    private String orderNumber;
    private OrderStatus status;
    @NotNull
    private String userId;
}
