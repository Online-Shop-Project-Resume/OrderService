package com.maksym.orderservice.service;

import com.maksym.orderservice.exception.EntityNotFoundException;
import com.maksym.orderservice.model.OrderItem;
import com.maksym.orderservice.repository.OrderItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderService orderService;

    public OrderItemService(OrderService orderService, OrderItemRepository orderItemRepository) {
        this.orderService = orderService;
        this.orderItemRepository = orderItemRepository;
    }

    public OrderItem create(OrderItem orderItem) {
        log.info("OrderItem create: {}", orderItem);
        orderItem.setOrder(orderService.getById(orderItem.getOrder().getId()));
        return orderItemRepository.save(orderItem);
    }

    public OrderItem getById(Long id) {
        log.info("OrderItem get by id: {}", id);
        return orderItemRepository.findById(id).orElseThrow(()->new EntityNotFoundException("OrderItem with id: " + id + " does not exist"));
    }

    public Page<OrderItem> getAll(Pageable pageable) {
        log.info("OrderItem get all: {}", pageable);
        return orderItemRepository.findAll(pageable);
    }

    public OrderItem updateById(Long id, OrderItem orderItem) {
        getById(id);
        orderItem.setId(id);
        orderItem.setOrder(orderService.getById(orderItem.getOrder().getId()));
        log.info("OrderItem update by id: {}", orderItem);
        return orderItemRepository.save(orderItem);
    }

    public Boolean deleteById(Long id) {
        log.info("OrderItem delete by id: {}", id);
        orderItemRepository.deleteById(id);
        return true;
    }
}
