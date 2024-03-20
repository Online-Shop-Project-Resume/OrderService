package com.maksym.orderservice.service;

import com.maksym.orderservice.dto.OrderItemRequest;
import com.maksym.orderservice.dto.OrderItemResponse;
import com.maksym.orderservice.dtoMapper.OrderItemMapper;
import com.maksym.orderservice.model.Order;
import com.maksym.orderservice.model.OrderItem;
import com.maksym.orderservice.repository.OrderItemRepository;
import com.maksym.orderservice.repository.OrderRepository;
import com.maksym.orderservice.staticObject.StaticOrder;
import com.maksym.orderservice.staticObject.StaticOrderItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceTest {

    private final Long id = 1L;
    @Mock
    private OrderItemRepository orderLineItemRepository;

    @InjectMocks
    private OrderItemServiceImpl orderItemService;
//    TODO
//    @Test
//    void testCreateOrderItem() {
//        // Arrange
//        OrderItemRequest request = StaticOrderItem.orderItemRequest();
//        OrderItem orderItem = StaticOrderItem.orderItem();
//        when(OrderItemMapper.toModel(any(OrderItemRequest.class))).thenReturn(orderItem); // Mocking OrderItemMapper
//
//        // Mocking orderRepository behavior if needed
//        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(new Order())); // Example mock for orderRepository
//
//        when(orderLineItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);
//
//        // Act
//        OrderItemResponse response = orderItemService.create(request);
//
//        // Assert
//        assertEquals(orderItem.getId(), response.getId());
//        assertEquals(orderItem.getOrder(), response.getOrder());
//        assertEquals(orderItem.getQuantity(), response.getQuantity());
//        assertEquals(orderItem.getSkuCode(), response.getSkuCode());
//        assertEquals(orderItem.getInventoryId(), response.getInventoryId());
//    }

    @Test
    void testGetOrderItemById() {
        // Arrange
        Long id = 1L;
        OrderItem orderItem = StaticOrderItem.orderItem();
        when(orderLineItemRepository.findById(id)).thenReturn(Optional.of(orderItem));

        // Act
        OrderItemResponse response = orderItemService.getById(id);

        // Assert
        assertEquals(orderItem.getId(), response.getId());
        assertEquals(orderItem.getOrder(), response.getOrder());
        assertEquals(orderItem.getQuantity(), response.getQuantity());
        assertEquals(orderItem.getSkuCode(), response.getSkuCode());
        assertEquals(orderItem.getInventoryId(), response.getInventoryId());
    }

    @Test
    void testGetAllOrderItems() {
        // Arrange
        OrderItem orderItem = StaticOrderItem.orderItem(); // Create your order item here
        when(orderLineItemRepository.findAll()).thenReturn(Collections.singletonList(orderItem));

        // Act
        List<OrderItemResponse> responseList = orderItemService.getAll();

        // Assert
        assertEquals(1, responseList.size());
        assertEquals(orderItem.getId(), responseList.get(0).getId());
        assertEquals(orderItem.getOrder(), responseList.get(0).getOrder());
        assertEquals(orderItem.getQuantity(), responseList.get(0).getQuantity());
        assertEquals(orderItem.getSkuCode(), responseList.get(0).getSkuCode());
        assertEquals(orderItem.getInventoryId(), responseList.get(0).getInventoryId());
    }

    @Test
    void testUpdateOrderItem() {
        // Arrange
        OrderItemRequest request = StaticOrderItem.orderItemRequest();
        OrderItem updatedOrderItem = StaticOrderItem.orderItem();
        updatedOrderItem.setId(id);

        when(orderLineItemRepository.save(any(OrderItem.class))).thenReturn(updatedOrderItem);

        // Act
        OrderItemResponse response = orderItemService.update(id, request);

        // Assert
        assertEquals(updatedOrderItem.getId(), response.getId());
        // Add more assertions as needed
    }

    @Test
    void testDeleteOrderItem() {
        when(orderLineItemRepository.existsById(id)).thenReturn(true);

        // Act
        orderItemService.delete(id);

        // Assert
        verify(orderLineItemRepository, times(1)).deleteById(id);
    }
}
