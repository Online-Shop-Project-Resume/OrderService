package com.maksym.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maksym.orderservice.dto.request.OrderItemDtoRequest;
import com.maksym.orderservice.dto.response.OrderItemDtoResponse;
import com.maksym.orderservice.model.OrderItem;
import com.maksym.orderservice.exception.EntityNotFoundException;
import com.maksym.orderservice.service.OrderItemService;
import com.maksym.orderservice.staticObject.StaticOrderItem;
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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderItemControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderItemService orderItemService;

    private final String DOCUMENTATION_URI = "http://swagger_documentation";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OrderItemDtoRequest orderItemRequest = StaticOrderItem.orderItemDtoRequest1();
    private final OrderItem orderItemModel = StaticOrderItem.orderItem1(); 
    private final OrderItemDtoResponse orderItemResponse = StaticOrderItem.orderItemDtoResponse1();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        OrderItemController orderItemController = new OrderItemController(orderItemService);
        mockMvc = MockMvcBuilders.standaloneSetup(orderItemController)
                .setControllerAdvice(new GlobalExceptionHandler(DOCUMENTATION_URI))
                .build();
    }

    @Test
    void testCreate_Success_ShouldReturnCreated() throws Exception {
        when(orderItemService.create(any(OrderItem.class))).thenReturn(orderItemModel);

        mockMvc.perform(post("/api/order-item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderItemRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(orderItemResponse.getId()))
                .andExpect(jsonPath("$.skuCode").value(orderItemResponse.getSkuCode()))
                .andExpect(jsonPath("$.quantity").value(orderItemResponse.getQuantity()))
                .andExpect(jsonPath("$.inventoryId").value(orderItemResponse.getInventoryId()))
                .andExpect(jsonPath("$.order.id").value(orderItemResponse.getOrder().getId()));
    }

    @Test
    void testCreate_InvalidInput_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/order-item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreate_EntityNotFoundException_ShouldReturnNotFound() throws Exception {
        when(orderItemService.create(any(OrderItem.class))).thenThrow(new EntityNotFoundException("OrderItem not found"));

        mockMvc.perform(post("/api/order-item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderItemRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("OrderItem not found"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }

    @Test
    void testCreate_AnyException_ShouldReturnBadRequest() throws Exception {
        doThrow(new DataAccessException("Database connection failed") {}).when(orderItemService).create(any(OrderItem.class));

        mockMvc.perform(post("/api/order-item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderItemRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Database connection failed"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }

    @Test
    void testGetById_Success_ShouldReturnOk() throws Exception {
        when(orderItemService.getById(StaticOrderItem.ID)).thenReturn(orderItemModel);

        mockMvc.perform(get("/api/order-item/{id}", StaticOrderItem.ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(orderItemResponse.getId()))
                .andExpect(jsonPath("$.skuCode").value(orderItemResponse.getSkuCode()))
                .andExpect(jsonPath("$.quantity").value(orderItemResponse.getQuantity()))
                .andExpect(jsonPath("$.inventoryId").value(orderItemResponse.getInventoryId()))
                .andExpect(jsonPath("$.order.id").value(orderItemResponse.getOrder().getId()));
    }

    @Test
    void testGetById_EntityNotFoundException_ShouldReturnNotFound() throws Exception {
        when(orderItemService.getById(any())).thenThrow(new EntityNotFoundException("OrderItem not found"));

        mockMvc.perform(get("/api/order-item/"+StaticOrderItem.ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("OrderItem not found"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }

    @Test
    void testGetById_AnyException_ShouldReturnBadRequest() throws Exception {
        doThrow(new DataAccessException("Database connection failed") {}).when(orderItemService).getById(any());

        mockMvc.perform(get("/api/order-item/"+StaticOrderItem.ID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Database connection failed"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }


    @Test
    void testGetAll_Success_ShouldReturnOk() throws Exception {
        List<OrderItem> orderItemList = Arrays.asList(orderItemModel, StaticOrderItem.orderItem1());
        Page<OrderItem> orderItemPage = new PageImpl<>(orderItemList);
        Pageable pageable = Pageable.unpaged();
        when(orderItemService.getAll(pageable)).thenReturn(orderItemPage);

        mockMvc.perform(get("/api/order-item/"))
                .andExpect(status().isOk())
		        .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id").value(orderItemResponse.getId()))
                .andExpect(jsonPath("$.[1].id").value(StaticOrderItem.orderItemDtoResponse2().getId()));
    }

    @Test
    void testGetAll_AnyException_ShouldReturnBadRequest() throws Exception {
        doThrow(new DataAccessException("Database connection failed") {}).when(orderItemService).getAll(any(Pageable.class));

        mockMvc.perform(get("/api/order-item/"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Database connection failed"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }


    @Test
    void testUpdateById_Success_ShouldReturnOk() throws Exception {
        when(orderItemService.updateById(any(), any(OrderItem.class))).thenReturn(orderItemModel);

        mockMvc.perform(put("/api/order-item/"+StaticOrderItem.ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderItemRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(orderItemResponse.getId()))
                .andExpect(jsonPath("$.skuCode").value(orderItemResponse.getSkuCode()))
                .andExpect(jsonPath("$.quantity").value(orderItemResponse.getQuantity()))
                .andExpect(jsonPath("$.inventoryId").value(orderItemResponse.getInventoryId()))
                .andExpect(jsonPath("$.order.id").value(orderItemResponse.getOrder().getId()));
    }

    @Test
    void testUpdateById_InvalidInput_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/api/order-item/"+StaticOrderItem.ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateById_EntityNotFoundException_ShouldReturnNotFound() throws Exception {
        when(orderItemService.updateById(any(), any(OrderItem.class))).thenThrow(new EntityNotFoundException("OrderItem not found"));

        mockMvc.perform(put("/api/order-item/"+StaticOrderItem.ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderItemRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("OrderItem not found"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }

    @Test
    void testUpdateById_AnyException_ShouldReturnBadRequest() throws Exception {
        doThrow(new DataAccessException("Database connection failed") {}).when(orderItemService).updateById(any(), any(OrderItem.class));

        mockMvc.perform(put("/api/order-item/"+StaticOrderItem.ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderItemRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Database connection failed"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }

    @Test
    void testDelete_Success_ShouldReturnNoContent() throws Exception {
        when(orderItemService.deleteById(StaticOrderItem.ID)).thenReturn(true);

        mockMvc.perform(delete("/api/order-item/"+StaticOrderItem.ID))
                .andExpect(status().isNoContent());
    }
	
    @Test
    void testDelete_AnyException_ShouldReturnBadRequest() throws Exception {
        doThrow(new DataAccessException("Database connection failed") {}).when(orderItemService).deleteById(StaticOrderItem.ID);

        mockMvc.perform(delete("/api/order-item/"+StaticOrderItem.ID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Database connection failed"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }
}