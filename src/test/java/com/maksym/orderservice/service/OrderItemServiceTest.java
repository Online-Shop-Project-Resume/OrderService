package com.maksym.orderservice.service;


import com.maksym.orderservice.exception.EntityNotFoundException;
import com.maksym.orderservice.model.OrderItem;
import com.maksym.orderservice.repository.OrderItemRepository;
import com.maksym.orderservice.staticObject.StaticOrderItem;
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

class OrderItemServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private OrderService orderService;
    @InjectMocks
    private OrderItemService orderItemService;
    private final OrderItem orderItem = StaticOrderItem.orderItem1();
    private final OrderItem orderItem2 = StaticOrderItem.orderItem2();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        when(orderService.getById(StaticOrder.ID)).thenReturn(StaticOrder.order1());
	    when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        OrderItem createdOrderItem = orderItemService.create(orderItem);

        assertNotNull(createdOrderItem);
        assertEquals(orderItem, createdOrderItem);
        verify(orderService, times(1)).getById(StaticOrder.ID);
        verify(orderItemRepository, times(1)).save(orderItem);
    }

    @Test
    void testCreate_EntityNotFoundException_OrderNotFound() {
        when(orderService.getById(StaticOrder.ID)).thenThrow(new EntityNotFoundException("Order not found"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> orderItemService.create(orderItem));

        assertNotNull(exception);
        assertEquals("Order not found", exception.getMessage());
        verify(orderService, times(1)).getById(StaticOrder.ID);
        verifyNoInteractions(orderItemRepository);
    }

    @Test
    void testCreate_DataAccessException() {
        when(orderService.getById(StaticOrder.ID)).thenReturn(StaticOrder.order1());
        when(orderItemRepository.findById(StaticOrderItem.ID)).thenThrow(new DataAccessException("Database connection failed") {
        });

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderItemService.getById(StaticOrderItem.ID));

        assertNotNull(exception);
        assertEquals("Database connection failed", exception.getMessage());
        verify(orderItemRepository, times(1)).findById(StaticOrderItem.ID);
    }

    @Test
    void testGetAll() {
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);
        orderItemList.add(orderItem2);
        Page<OrderItem> orderItemPage = new PageImpl<>(orderItemList);
        Pageable pageable = Pageable.unpaged();
        when(orderItemRepository.findAll(pageable)).thenReturn(orderItemPage);

        Page<OrderItem> result = orderItemService.getAll(pageable);

        assertEquals(orderItemList.size(), result.getSize());
        assertEquals(orderItem, result.getContent().get(0));
        assertEquals(orderItem2, result.getContent().get(1));
    }

    @Test
    void testGetAll_AnyException() {
        when(orderItemRepository.findAll(any(Pageable.class))).thenThrow(new DataAccessException("Database connection failed") {});

        Pageable pageable = Pageable.unpaged();
        RuntimeException exception = assertThrows(DataAccessException.class, () -> orderItemService.getAll(pageable));

        assertNotNull(exception);
        assertEquals("Database connection failed", exception.getMessage());
        verify(orderItemRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testUpdate_Success() {
	    OrderItem existingOrderItem = StaticOrderItem.orderItem1();
        OrderItem updatedOrderItem = StaticOrderItem.orderItem2();
        when(orderService.getById(StaticOrder.ID)).thenReturn(StaticOrder.order1());
	    when(orderItemRepository.findById(StaticOrderItem.ID)).thenReturn(java.util.Optional.of(existingOrderItem));
        when(orderItemRepository.save(updatedOrderItem)).thenReturn(updatedOrderItem);

        OrderItem result = orderItemService.updateById(StaticOrderItem.ID, updatedOrderItem);

        assertEquals(updatedOrderItem, result);
        verify(orderItemRepository, times(1)).findById(StaticOrderItem.ID);
        verify(orderItemRepository, times(1)).save(updatedOrderItem);
    }

    @Test
    void testUpdateById_EntityNotFoundException_OrderNotFound() {
        when(orderItemRepository.findById(StaticOrderItem.ID)).thenReturn(java.util.Optional.of(orderItem));
        when(orderService.getById(StaticOrder.ID)).thenThrow(new EntityNotFoundException("Order not found"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> orderItemService.updateById(StaticOrderItem.ID, orderItem));

        assertNotNull(exception);
        assertEquals("Order not found", exception.getMessage());
        verify(orderService, times(1)).getById(StaticOrder.ID);
    }


    @Test
    void testUpdateById_EntityNotFoundException() {
        OrderItem updatedOrderItem = StaticOrderItem.orderItem1();
        when(orderItemRepository.findById(StaticOrderItem.ID)).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderItemService.updateById(StaticOrderItem.ID, updatedOrderItem));
        verify(orderItemRepository, times(1)).findById(StaticOrderItem.ID);
        verify(orderItemRepository, never()).save(updatedOrderItem);
    }

    @Test
    void testUpdateById_AnyException() {
        OrderItem existingOrderItem = StaticOrderItem.orderItem1();
        OrderItem updatedOrderItem = StaticOrderItem.orderItem2();
        when(orderItemRepository.findById(StaticOrderItem.ID)).thenReturn(java.util.Optional.of(existingOrderItem));
        when(orderService.getById(StaticOrder.ID)).thenReturn(StaticOrder.order1());
	    when(orderItemRepository.save(updatedOrderItem)).thenThrow(new DataAccessException("Database connection failed") {
        });

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderItemService.updateById(StaticOrderItem.ID, updatedOrderItem));

        assertNotNull(exception);
        assertEquals("Database connection failed", exception.getMessage());
        verify(orderItemRepository, times(1)).save(updatedOrderItem);
    }

    @Test
    void testDeleteById_Success() {
        boolean result = orderItemService.deleteById(StaticOrderItem.ID);

        verify(orderItemRepository).deleteById(StaticOrderItem.ID);
        assertTrue(result);
    }

    @Test
    void testDeleteById_AnyException() {
        doThrow(new DataAccessException("Database connection failed") {}).when(orderItemRepository).deleteById(StaticOrderItem.ID);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderItemService.deleteById(StaticOrderItem.ID));

        assertNotNull(exception);
        assertEquals("Database connection failed", exception.getMessage());
        verify(orderItemRepository, times(1)).deleteById(StaticOrderItem.ID);
    }
}