package com.maksym.orderservice.service;

import com.maksym.orderservice.dto.OrderItemRequest;
import com.maksym.orderservice.dto.OrderItemResponse;

import java.util.List;

public interface OrderItemService {
    public OrderItemResponse create(OrderItemRequest orderItemRequestDto);
    public OrderItemResponse getById(Long id);
    public List<OrderItemResponse> getAll();
    public OrderItemResponse update(Long id, OrderItemRequest orderItemRequestDto);
    void delete(Long id);
}
