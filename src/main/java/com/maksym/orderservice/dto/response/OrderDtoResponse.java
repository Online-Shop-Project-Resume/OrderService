package com.maksym.orderservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDtoResponse {

    private Long id;

    private String orderNumber;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")

    private LocalDateTime orderDate;

    private String inventoryId;

    private OrderStatus status;

    private String userId;
}
