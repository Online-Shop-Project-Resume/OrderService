package com.maksym.orderservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDtoRequest {

    @NotNull(message = "Order Number cannot be null")
    @NotBlank(message = "Order Number cannot be blank")
    private String orderNumber;

    @NotNull(message = "Inventory Id cannot be null")
    @NotBlank(message = "Inventory Id cannot be blank")
    private String inventoryId;

    @NotNull(message = "Status cannot be null")
    @NotBlank(message = "Status cannot be blank")
    private String status;

    @NotNull(message = "User Id cannot be null")
    @NotBlank(message = "User Id cannot be blank")
    private String userId;
}
