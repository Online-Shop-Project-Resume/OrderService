package com.maksym.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maksym.orderservice.dto.OrderItemRequest;
import com.maksym.orderservice.dto.OrderItemResponse;
import com.maksym.orderservice.exception.GlobalExceptionHandler;
import com.maksym.orderservice.service.OrderItemServiceImpl;
import com.maksym.orderservice.staticObject.StaticOrderItem;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderItemControllerTest {

    private final Long id = 1L;
    @Mock
    private OrderItemServiceImpl orderItemService;

    @InjectMocks
    private OrderItemController orderItemController;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(orderItemController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();
    }


    @Test
    void createOrderItem_Success() throws Exception {
        // Arrange
        OrderItemRequest orderItemRequest = StaticOrderItem.orderItemRequest();
        OrderItemResponse orderItemResponse = StaticOrderItem.orderItemResponse();
        when(orderItemService.create(ArgumentMatchers.any())).thenReturn(orderItemResponse);

        // Act
        ResponseEntity<Object> response = orderItemController.create(orderItemRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    // Add similar tests for other controller methods (get, getAll, update, delete)

    @Test
    public void testCreateInvalidOrderItem() throws Exception {

        // Perform the POST request to create an order item
        mockMvc.perform(post("/api/order-item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"OrderName\": \"\" }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetNonexistentOrderItem() throws Exception {
        // Mock the service to throw a NotFoundException
        when(orderItemService.getById(id)).thenThrow(EntityNotFoundException.class);

        // Perform the GET request to retrieve the order item
        mockMvc.perform(get("/api/order-item/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetOrderItemById() throws Exception {
        OrderItemResponse response = StaticOrderItem.orderItemResponse();

        when(orderItemService.getById(id)).thenReturn(response);

        mockMvc.perform(get("/api/order-item/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                // Add more assertions for other fields if needed
                .andReturn();
    }

    @Test
    void testGetAllOrderItems() throws Exception {
        List<OrderItemResponse> responseList = List.of(StaticOrderItem.orderItemResponse());
        // Populate responseList with sample data

        when(orderItemService.getAll()).thenReturn(responseList);

        mockMvc.perform(get("/api/order-item"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(responseList.size())))
                // Add more assertions for the content of the list if needed
                .andReturn();
    }

    @Test
    void testUpdateOrderItem() throws Exception {
        OrderItemRequest request = StaticOrderItem.orderItemRequest();
        // Set up request data

        OrderItemResponse response = new OrderItemResponse();
        // Set up expected response data

        when(orderItemService.update(id, request)).thenReturn(response);

        mockMvc.perform(put("/api/order-item/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                // Add more assertions for other fields if needed
                .andReturn();
    }

    @Test
    void testDeleteOrderItem() throws Exception {
        mockMvc.perform(delete("/api/order-item/{id}", id))
                .andExpect(status().isNoContent())
                .andReturn();

        verify(orderItemService, times(1)).delete(id);
    }
}

