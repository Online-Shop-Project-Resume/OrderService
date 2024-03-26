package com.maksym.orderservice.controller;

import com.maksym.orderservice.dto.OrderRequest;
import com.maksym.orderservice.service.OrderServiceImpl;
import com.maksym.orderservice.util.enums.OrderStatus;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderServiceImpl orderServiceImpl;

    public OrderController(OrderServiceImpl orderServiceImpl) {
        this.orderServiceImpl = orderServiceImpl;
    }

    @PostMapping
    public ResponseEntity<Object> createOrder(@RequestBody @Valid OrderRequest orderRequest) {
        return new ResponseEntity<>(orderServiceImpl.createOrder(orderRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOrder(@PathVariable("id") Long id) {
        return new ResponseEntity<>(orderServiceImpl.getOrderById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getAllOrders() {
        return new ResponseEntity<>(orderServiceImpl.getAllOrders(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateOrder(@PathVariable("id") Long id, @RequestBody @Valid OrderRequest orderRequest) {
        return new ResponseEntity<>(orderServiceImpl.updateOrder(id, orderRequest), HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<Object> updateStatusOrder(@PathVariable("id") Long id, @RequestParam("status") String status) {
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        return new ResponseEntity<>(orderServiceImpl.updateStatusOrder(id, orderStatus), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable("id") Long id) {
        orderServiceImpl.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
