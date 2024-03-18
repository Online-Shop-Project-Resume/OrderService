package com.maksym.orderservice.controller;

import com.maksym.orderservice.dto.OrderItemRequest;
import com.maksym.orderservice.service.OrderItemServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-item")
public class OrderItemController {

    private final OrderItemServiceImpl orderLineItemService;

    public OrderItemController(OrderItemServiceImpl orderLineItemService) {
        this.orderLineItemService = orderLineItemService;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody OrderItemRequest orderItemRequestDto) {
        return new ResponseEntity<>(orderLineItemService.create(orderItemRequestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") Long id) {
        return new ResponseEntity<>(orderLineItemService.getById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return new ResponseEntity<>(orderLineItemService.getAll(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody OrderItemRequest orderItemRequestDto) {
        return new ResponseEntity<>(orderLineItemService.update(id, orderItemRequestDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        orderLineItemService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
