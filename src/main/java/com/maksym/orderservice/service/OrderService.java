package com.maksym.orderservice.service;

import com.maksym.orderservice.exception.EntityNotFoundException;
import com.maksym.orderservice.model.Order;
import com.maksym.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order create(Order order) {
        log.info("Order create: {}", order);

        return orderRepository.save(order);
    }

    public Order getById(Long id) {
        log.info("Order get by id: {}", id);
        return orderRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Order with id: " + id + " does not exist"));
    }

    public Page<Order> getAll(Pageable pageable) {
        log.info("Order get all: {}", pageable);
        return orderRepository.findAll(pageable);
    }

    public Order updateById(Long id, Order order) {
        getById(id);
        order.setId(id);

        log.info("Order update by id: {}", order);
        return orderRepository.save(order);
    }

    public Boolean deleteById(Long id) {
        log.info("Order delete by id: {}", id);
        orderRepository.deleteById(id);
        return true;
    }
}
