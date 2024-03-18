package com.maksym.orderservice.controller;

import com.maksym.orderservice.dto.OrderRequest;
import com.maksym.orderservice.service.OrderServiceImpl;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
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
    @CircuitBreaker(name="inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name="inventory")
    public CompletableFuture<ResponseEntity<Object>> createOrder(@RequestBody OrderRequest orderRequest) {
        return CompletableFuture.supplyAsync(()->new ResponseEntity<>(orderServiceImpl.createOrder(orderRequest), HttpStatus.CREATED));
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
    public ResponseEntity<Object> updateOrder(@PathVariable("id") Long id, @RequestBody OrderRequest orderRequest) {
        return new ResponseEntity<>(orderServiceImpl.updateOrder(id, orderRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable("id") Long id) {
        orderServiceImpl.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(()->"Oops, something went wrong!");
    }
}
