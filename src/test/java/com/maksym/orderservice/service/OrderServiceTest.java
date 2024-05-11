package com.maksym.orderservice.service;


import com.maksym.orderservice.exception.EntityNotFoundException;
import com.maksym.orderservice.model.Order;
import com.maksym.orderservice.repository.OrderRepository;
import com.maksym.orderservice.staticObject.StaticOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderService orderService;
    private final Order order = StaticOrder.order1();
    private final Order order2 = StaticOrder.order2();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
	    when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order createdOrder = orderService.create(order);

        assertNotNull(createdOrder);
        assertEquals(order, createdOrder);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testCreate_DataAccessException() {
        when(orderRepository.findById(StaticOrder.ID)).thenThrow(new DataAccessException("Database connection failed") {
        });

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.getById(StaticOrder.ID));

        assertNotNull(exception);
        assertEquals("Database connection failed", exception.getMessage());
        verify(orderRepository, times(1)).findById(StaticOrder.ID);
    }

    @Test
    void testGetAll() {
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        orderList.add(order2);
        Page<Order> orderPage = new PageImpl<>(orderList);
        Pageable pageable = Pageable.unpaged();
        when(orderRepository.findAll(pageable)).thenReturn(orderPage);

        Page<Order> result = orderService.getAll(pageable);

        assertEquals(orderList.size(), result.getSize());
        assertEquals(order, result.getContent().get(0));
        assertEquals(order2, result.getContent().get(1));
    }

    @Test
    void testGetAll_AnyException() {
        when(orderRepository.findAll(any(Pageable.class))).thenThrow(new DataAccessException("Database connection failed") {});

        Pageable pageable = Pageable.unpaged();
        RuntimeException exception = assertThrows(DataAccessException.class, () -> orderService.getAll(pageable));

        assertNotNull(exception);
        assertEquals("Database connection failed", exception.getMessage());
        verify(orderRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testUpdate_Success() {
	    Order existingOrder = StaticOrder.order1();
        Order updatedOrder = StaticOrder.order2();
	    when(orderRepository.findById(StaticOrder.ID)).thenReturn(java.util.Optional.of(existingOrder));
        when(orderRepository.save(updatedOrder)).thenReturn(updatedOrder);

        Order result = orderService.updateById(StaticOrder.ID, updatedOrder);

        assertEquals(updatedOrder, result);
        verify(orderRepository, times(1)).findById(StaticOrder.ID);
        verify(orderRepository, times(1)).save(updatedOrder);
    }


    @Test
    void testUpdateById_EntityNotFoundException() {
        Order updatedOrder = StaticOrder.order1();
        when(orderRepository.findById(StaticOrder.ID)).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.updateById(StaticOrder.ID, updatedOrder));
        verify(orderRepository, times(1)).findById(StaticOrder.ID);
        verify(orderRepository, never()).save(updatedOrder);
    }

    @Test
    void testUpdateById_AnyException() {
        Order existingOrder = StaticOrder.order1();
        Order updatedOrder = StaticOrder.order2();
        when(orderRepository.findById(StaticOrder.ID)).thenReturn(java.util.Optional.of(existingOrder));
	    when(orderRepository.save(updatedOrder)).thenThrow(new DataAccessException("Database connection failed") {
        });

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.updateById(StaticOrder.ID, updatedOrder));

        assertNotNull(exception);
        assertEquals("Database connection failed", exception.getMessage());
        verify(orderRepository, times(1)).save(updatedOrder);
    }

    @Test
    void testDeleteById_Success() {
        boolean result = orderService.deleteById(StaticOrder.ID);

        verify(orderRepository).deleteById(StaticOrder.ID);
        assertTrue(result);
    }

    @Test
    void testDeleteById_AnyException() {
        doThrow(new DataAccessException("Database connection failed") {}).when(orderRepository).deleteById(StaticOrder.ID);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.deleteById(StaticOrder.ID));

        assertNotNull(exception);
        assertEquals("Database connection failed", exception.getMessage());
        verify(orderRepository, times(1)).deleteById(StaticOrder.ID);
    }
}