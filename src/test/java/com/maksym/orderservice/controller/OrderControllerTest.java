package com.maksym.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maksym.orderservice.dto.request.OrderDtoRequest;
import com.maksym.orderservice.dto.response.OrderDtoResponse;
import com.maksym.orderservice.model.Order;
import com.maksym.orderservice.exception.EntityNotFoundException;
import com.maksym.orderservice.service.OrderService;
import com.maksym.orderservice.staticObject.StaticOrder;
import com.maksym.orderservice.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    private final String DOCUMENTATION_URI = "http://swagger_documentation";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private final OrderDtoRequest orderRequest = StaticOrder.orderDtoRequest1();
    private final Order orderModel = StaticOrder.order1(); 
    private final OrderDtoResponse orderResponse = StaticOrder.orderDtoResponse1();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        OrderController orderController = new OrderController(orderService);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(new GlobalExceptionHandler(DOCUMENTATION_URI))
                .build();
    }

    @Test
    void testCreate_Success_ShouldReturnCreated() throws Exception {
        when(orderService.create(any(Order.class))).thenReturn(orderModel);

        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(orderResponse.getId()))
                .andExpect(jsonPath("$.orderNumber").value(orderResponse.getOrderNumber()))
                .andExpect(jsonPath("$.orderDate").value(orderResponse.getOrderDate().format(formatter)))
                .andExpect(jsonPath("$.inventoryId").value(orderResponse.getInventoryId()))
                .andExpect(jsonPath("$.status").value(orderResponse.getStatus()))
                .andExpect(jsonPath("$.userId").value(orderResponse.getUserId()));
    }

    @Test
    void testCreate_InvalidInput_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreate_EntityNotFoundException_ShouldReturnNotFound() throws Exception {
        when(orderService.create(any(Order.class))).thenThrow(new EntityNotFoundException("Order not found"));

        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Order not found"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }

    @Test
    void testCreate_AnyException_ShouldReturnBadRequest() throws Exception {
        doThrow(new DataAccessException("Database connection failed") {}).when(orderService).create(any(Order.class));

        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Database connection failed"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }

    @Test
    void testGetById_Success_ShouldReturnOk() throws Exception {
        when(orderService.getById(StaticOrder.ID)).thenReturn(orderModel);

        mockMvc.perform(get("/api/order/{id}", StaticOrder.ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(orderResponse.getId()))
                .andExpect(jsonPath("$.orderNumber").value(orderResponse.getOrderNumber()))
                .andExpect(jsonPath("$.orderDate").value(orderResponse.getOrderDate().format(formatter)))
                .andExpect(jsonPath("$.inventoryId").value(orderResponse.getInventoryId()))
                .andExpect(jsonPath("$.status").value(orderResponse.getStatus()))
                .andExpect(jsonPath("$.userId").value(orderResponse.getUserId()));
    }

    @Test
    void testGetById_EntityNotFoundException_ShouldReturnNotFound() throws Exception {
        when(orderService.getById(any())).thenThrow(new EntityNotFoundException("Order not found"));

        mockMvc.perform(get("/api/order/"+StaticOrder.ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Order not found"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }

    @Test
    void testGetById_AnyException_ShouldReturnBadRequest() throws Exception {
        doThrow(new DataAccessException("Database connection failed") {}).when(orderService).getById(any());

        mockMvc.perform(get("/api/order/"+StaticOrder.ID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Database connection failed"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }


    @Test
    void testGetAll_Success_ShouldReturnOk() throws Exception {
        List<Order> orderList = Arrays.asList(orderModel, StaticOrder.order1());
        Page<Order> orderPage = new PageImpl<>(orderList);
        Pageable pageable = Pageable.unpaged();
        when(orderService.getAll(pageable)).thenReturn(orderPage);

        mockMvc.perform(get("/api/order/"))
                .andExpect(status().isOk())
		        .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id").value(orderResponse.getId()))
                .andExpect(jsonPath("$.[1].id").value(StaticOrder.orderDtoResponse2().getId()));
    }

    @Test
    void testGetAll_AnyException_ShouldReturnBadRequest() throws Exception {
        doThrow(new DataAccessException("Database connection failed") {}).when(orderService).getAll(any(Pageable.class));

        mockMvc.perform(get("/api/order/"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Database connection failed"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }


    @Test
    void testUpdateById_Success_ShouldReturnOk() throws Exception {
        when(orderService.updateById(any(), any(Order.class))).thenReturn(orderModel);

        mockMvc.perform(put("/api/order/"+StaticOrder.ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(orderResponse.getId()))
                .andExpect(jsonPath("$.orderNumber").value(orderResponse.getOrderNumber()))
                .andExpect(jsonPath("$.orderDate").value(orderResponse.getOrderDate().format(formatter)))
                .andExpect(jsonPath("$.inventoryId").value(orderResponse.getInventoryId()))
                .andExpect(jsonPath("$.status").value(orderResponse.getStatus()))
                .andExpect(jsonPath("$.userId").value(orderResponse.getUserId()));
    }

    @Test
    void testUpdateById_InvalidInput_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/api/order/"+StaticOrder.ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateById_EntityNotFoundException_ShouldReturnNotFound() throws Exception {
        when(orderService.updateById(any(), any(Order.class))).thenThrow(new EntityNotFoundException("Order not found"));

        mockMvc.perform(put("/api/order/"+StaticOrder.ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Order not found"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }

    @Test
    void testUpdateById_AnyException_ShouldReturnBadRequest() throws Exception {
        doThrow(new DataAccessException("Database connection failed") {}).when(orderService).updateById(any(), any(Order.class));

        mockMvc.perform(put("/api/order/"+StaticOrder.ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Database connection failed"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }

    @Test
    void testDelete_Success_ShouldReturnNoContent() throws Exception {
        when(orderService.deleteById(StaticOrder.ID)).thenReturn(true);

        mockMvc.perform(delete("/api/order/"+StaticOrder.ID))
                .andExpect(status().isNoContent());
    }
	
    @Test
    void testDelete_AnyException_ShouldReturnBadRequest() throws Exception {
        doThrow(new DataAccessException("Database connection failed") {}).when(orderService).deleteById(StaticOrder.ID);

        mockMvc.perform(delete("/api/order/"+StaticOrder.ID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Database connection failed"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }
}