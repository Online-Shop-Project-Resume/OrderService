package com.maksym.orderservice.service;

import com.maksym.orderservice.dto.OrderRequest;
import com.maksym.orderservice.dto.OrderResponse;
import com.maksym.orderservice.model.Order;
import com.maksym.orderservice.repository.OrderRepository;
import com.maksym.orderservice.staticObject.StaticOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private final Long id = 1L;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    public void testCreateOrder() {
        // Given
        OrderRequest orderRequest = StaticOrder.orderRequest();
        Order order = StaticOrder.order1();
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // When
        OrderResponse response = orderService.createOrder(orderRequest);

        // Then
        assertNotNull(response);
        // Add more assertions as needed
    }

    @Test
    public void testGetOrderById() {
        Order order = StaticOrder.order1();
        when(orderRepository.findById(id)).thenReturn(Optional.of(order));

        // When
        OrderResponse response = orderService.getOrderById(id);

        // Then
        assertNotNull(response);
        // Add more assertions as needed
    }

    @Test
    public void testGetAllOrders() {
        // Given
        List<Order> orders = new ArrayList<>();
        when(orderRepository.findAll()).thenReturn(orders);

        // When
        List<OrderResponse> responses = orderService.getAllOrders();

        // Then
        assertNotNull(responses);
        // Add more assertions as needed
    }

    @Test
    public void testUpdateOrder() {
        // Given
        OrderRequest orderRequest = new OrderRequest();
        Order order = new Order();
        when(orderRepository.existsById(id)).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // When
        OrderResponse response = orderService.updateOrder(id, orderRequest);

        // Then
        assertNotNull(response);
        // Add more assertions as needed
    }

    @Test
    public void testDeleteOrder() {
        // When
        orderService.deleteOrder(id);

        // Then
        verify(orderRepository).deleteById(id);
    }
}

