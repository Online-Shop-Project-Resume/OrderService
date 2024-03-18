package com.maksym.orderservice.service;

import com.maksym.orderservice.dto.OrderRequest;
import com.maksym.orderservice.dto.OrderResponse;
import com.maksym.orderservice.dtoMapper.OrderMapper;
import com.maksym.orderservice.model.Order;
import com.maksym.orderservice.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Transactional
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;


    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        log.info("Order create: {}", orderRequest);
        Order order = OrderMapper.toModel(orderRequest);
        return OrderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        log.info("Order get by id: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with id " + id + " not found"));

        return OrderMapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        log.info("Order get all");
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(OrderMapper::toResponse).toList();
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderRequest order) {
        log.info("Order update by id: {}, OrderRequest: {}", id, order);
        if(!orderRepository.existsById(id)) throw new EntityNotFoundException("Order with id: "+id+" is not found");
        Order updatedOrder = OrderMapper.toModel(order);
        updatedOrder.setId(id);
        return OrderMapper.toResponse(orderRepository.save(updatedOrder));
    }

    @Override
    public void deleteOrder(Long id) {
        log.info("Order delete by id: {}", id);
        orderRepository.deleteById(id);
    }
}
