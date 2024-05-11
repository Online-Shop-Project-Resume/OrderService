package com.maksym.orderservice.controller;

import com.maksym.orderservice.dto.mapper.OrderDtoMapper;
import com.maksym.orderservice.dto.request.OrderDtoRequest;
import com.maksym.orderservice.dto.response.OrderDtoResponse;
import com.maksym.orderservice.model.Order;
import com.maksym.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Create an order", description = "Create new order")
    @ApiResponse(responseCode = "201", description = "Order saved successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Invalid foreign key that is not found")
    @ApiResponse(responseCode = "503", description = "Database connection failed")
    public ResponseEntity<OrderDtoResponse> createOrder(@Valid @RequestBody OrderDtoRequest orderDtoRequest) {
        Order order = OrderDtoMapper.toModel(orderDtoRequest);
        order = orderService.create(order);
        return new ResponseEntity<>(OrderDtoMapper.toResponse(order), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Order", description = "Get Order By Id")
    @ApiResponse(responseCode = "200", description = "Order Get successfully")
    @ApiResponse(responseCode = "404", description = "Order with such an Id not found")
    public ResponseEntity<OrderDtoResponse> getOrderById(@PathVariable("id") Long id) {
        Order order = orderService.getById(id);
        return new ResponseEntity<>(OrderDtoMapper.toResponse(order), HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Get All Order", description = "Get All Order")
    @ApiResponse(responseCode = "200", description = "Order Get All successfully")
    @ApiResponse(responseCode = "404", description = "No records with Order have been found")
    public ResponseEntity<Page<OrderDtoResponse>> getAllOrder(Pageable pageable) {
        Page<Order> orderPage = orderService.getAll(pageable);
        return new ResponseEntity<>(orderPage.map(OrderDtoMapper::toResponse), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an order", description = "Update an order by Id and new Order")
    @ApiResponse(responseCode = "201", description = "Order updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Order with such an Id not found or invalid foreign key that is not found")
    public ResponseEntity<OrderDtoResponse> updateOrder(@PathVariable("id") Long id, @Valid @RequestBody OrderDtoRequest orderDtoRequest) {
        Order order = OrderDtoMapper.toModel(orderDtoRequest);
        order = orderService.updateById(id, order);
        return new ResponseEntity<>(OrderDtoMapper.toResponse(order), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an order", description = "Delete an order by id")
    @ApiResponse(responseCode = "204", description = "Order deleted successfully")
    public ResponseEntity<Boolean> deleteOrder(@PathVariable("id") Long id) {
        return new ResponseEntity<>(orderService.deleteById(id), HttpStatus.NO_CONTENT);
    }
}