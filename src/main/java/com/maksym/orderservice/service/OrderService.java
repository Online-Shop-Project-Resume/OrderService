package com.maksym.orderservice.service;

import com.maksym.orderservice.dto.OrderRequest;
import com.maksym.orderservice.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest order);
    OrderResponse getOrderById(Long id);
    List<OrderResponse> getAllOrders();
    OrderResponse updateOrder(Long id, OrderRequest order);
    void deleteOrder(Long id);
}
