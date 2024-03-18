package com.maksym.orderservice.service;

import com.maksym.orderservice.dto.OrderItemRequest;
import com.maksym.orderservice.dto.OrderItemResponse;

import java.util.List;

public interface OrderItemService {
    OrderItemResponse create(OrderItemRequest orderItemRequestDto);
    OrderItemResponse getById(Long id);
    List<OrderItemResponse> getAll();
    OrderItemResponse update(Long id, OrderItemRequest orderItemRequestDto);
    void delete(Long id);
}
