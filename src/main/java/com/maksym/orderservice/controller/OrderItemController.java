package com.maksym.orderservice.controller;

import com.maksym.orderservice.dto.mapper.OrderItemDtoMapper;
import com.maksym.orderservice.dto.request.OrderItemDtoRequest;
import com.maksym.orderservice.dto.response.OrderItemDtoResponse;
import com.maksym.orderservice.model.OrderItem;
import com.maksym.orderservice.service.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-item")
public class OrderItemController {
    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PostMapping
    @Operation(summary = "Create an orderItem", description = "Create new orderItem")
    @ApiResponse(responseCode = "201", description = "OrderItem saved successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Invalid foreign key that is not found")
    @ApiResponse(responseCode = "503", description = "Database connection failed")
    public ResponseEntity<OrderItemDtoResponse> createOrderItem(@Valid @RequestBody OrderItemDtoRequest orderItemDtoRequest) {
        OrderItem orderItem = OrderItemDtoMapper.toModel(orderItemDtoRequest);
        orderItem = orderItemService.create(orderItem);
        return new ResponseEntity<>(OrderItemDtoMapper.toResponse(orderItem), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get OrderItem", description = "Get OrderItem By Id")
    @ApiResponse(responseCode = "200", description = "OrderItem Get successfully")
    @ApiResponse(responseCode = "404", description = "OrderItem with such an Id not found")
    public ResponseEntity<OrderItemDtoResponse> getOrderItemById(@PathVariable("id") Long id) {
        OrderItem orderItem = orderItemService.getById(id);
        return new ResponseEntity<>(OrderItemDtoMapper.toResponse(orderItem), HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Get All OrderItem", description = "Get All OrderItem")
    @ApiResponse(responseCode = "200", description = "OrderItem Get All successfully")
    @ApiResponse(responseCode = "404", description = "No records with OrderItem have been found")
    public ResponseEntity<Page<OrderItemDtoResponse>> getAllOrderItem(Pageable pageable) {
        Page<OrderItem> orderItemPage = orderItemService.getAll(pageable);
        return new ResponseEntity<>(orderItemPage.map(OrderItemDtoMapper::toResponse), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an orderItem", description = "Update an orderItem by Id and new OrderItem")
    @ApiResponse(responseCode = "201", description = "OrderItem updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "OrderItem with such an Id not found or invalid foreign key that is not found")
    public ResponseEntity<OrderItemDtoResponse> updateOrderItem(@PathVariable("id") Long id, @Valid @RequestBody OrderItemDtoRequest orderItemDtoRequest) {
        OrderItem orderItem = OrderItemDtoMapper.toModel(orderItemDtoRequest);
        orderItem = orderItemService.updateById(id, orderItem);
        return new ResponseEntity<>(OrderItemDtoMapper.toResponse(orderItem), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an orderItem", description = "Delete an orderItem by id")
    @ApiResponse(responseCode = "204", description = "OrderItem deleted successfully")
    public ResponseEntity<Boolean> deleteOrderItem(@PathVariable("id") Long id) {
        return new ResponseEntity<>(orderItemService.deleteById(id), HttpStatus.NO_CONTENT);
    }
}