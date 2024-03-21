package com.maksym.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maksym.orderservice.dto.OrderRequest;
import com.maksym.orderservice.dto.OrderResponse;
import com.maksym.orderservice.exception.GlobalExceptionHandler;
import com.maksym.orderservice.service.OrderServiceImpl;
import com.maksym.orderservice.staticObject.StaticOrder;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    private final Long id = 1L;
    @Mock
    private OrderServiceImpl orderService;
    @InjectMocks
    private OrderController orderController;
    private MockMvc mockMvc;

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();
    }
    @Test
    void testCreateOrder() throws Exception {
        OrderRequest orderRequest = StaticOrder.orderRequest();
        OrderResponse orderResponse = StaticOrder.orderResponse1();
        when(orderService.createOrder(any(OrderRequest.class))).thenReturn(orderResponse);

        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(orderResponse.getId()))
                .andExpect(jsonPath("$.orderNumber").value(orderResponse.getOrderNumber()))
                .andExpect(jsonPath("$.status").value(orderResponse.getStatus().toString()))
                .andExpect(jsonPath("$.userId").value(orderResponse.getUserId()));
    }

    // Similar tests for other endpoints: getOrder, getAllOrders, updateOrder, deleteOrder

    @Test
    void testHandleConstraintViolationException() throws Exception {
        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"orderNumber\": \"\" }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testHandleEntityNotFoundException() throws Exception {
        String errorMessage = "Order not found";
        EntityNotFoundException exception = new EntityNotFoundException(errorMessage);
        when(orderService.getOrderById(anyLong())).thenThrow(exception);

        mockMvc.perform(get("/api/order/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMessage));
    }

    @Test
    void testGetOrder() throws Exception {
        OrderResponse orderResponse = StaticOrder.orderResponse1();
        when(orderService.getOrderById(id)).thenReturn(orderResponse);

        mockMvc.perform(get("/api/order/{id}", id))
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderResponse.getId()))
                .andExpect(jsonPath("$.orderNumber").value(orderResponse.getOrderNumber()))
                .andExpect(jsonPath("$.status").value(orderResponse.getStatus().toString()))
                .andExpect(jsonPath("$.userId").value(orderResponse.getUserId()));
    }

    @Test
    void testGetAllOrders() throws Exception {
        List<OrderResponse> orderResponses = List.of(StaticOrder.orderResponse1());
        when(orderService.getAllOrders()).thenReturn(orderResponses);

        mockMvc.perform(get("/api/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(orderResponses.size()));
    }

    @Test
    void testUpdateOrder() throws Exception {
        long orderId = 1L;
        OrderRequest orderRequest = StaticOrder.orderRequest();
        OrderResponse orderResponse = StaticOrder.orderResponse1();
        when(orderService.updateOrder(orderId, orderRequest)).thenReturn(orderResponse);

        mockMvc.perform(put("/api/order/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderResponse.getId()))
                .andExpect(jsonPath("$.orderNumber").value(orderResponse.getOrderNumber()))
                .andExpect(jsonPath("$.status").value(orderResponse.getStatus().toString()))
                .andExpect(jsonPath("$.userId").value(orderResponse.getUserId()));
    }

    @Test
    void testDeleteOrder() throws Exception {

        mockMvc.perform(delete("/api/order/{id}", id))
                .andExpect(status().isNoContent());
    }
}
