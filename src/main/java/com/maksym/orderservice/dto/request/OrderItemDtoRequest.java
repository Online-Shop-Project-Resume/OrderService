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
public class OrderItemDtoRequest {

    @NotNull(message = "Sku Code cannot be null")
    @NotBlank(message = "Sku Code cannot be blank")
    private String skuCode;

    @Positive(message = "Quantity must be a positive number")
    @NotNull(message = "Quantity cannot be null")
    private Integer quantity;

    @NotNull(message = "Inventory Id cannot be null")
    @NotBlank(message = "Inventory Id cannot be blank")
    private String inventoryId;

    @Positive(message = "Order must be a positive number")
    @NotNull(message = "Order cannot be null")
    private Long orderId;
}
