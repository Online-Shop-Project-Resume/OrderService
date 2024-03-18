package com.maksym.orderservice.service;

import com.maksym.orderservice.dto.OrderItemRequest;
import com.maksym.orderservice.dto.OrderItemResponse;
import com.maksym.orderservice.dtoMapper.OrderItemMapper;
import com.maksym.orderservice.model.Order;
import com.maksym.orderservice.model.OrderItem;
import com.maksym.orderservice.repository.OrderLineItemRepository;
import com.maksym.orderservice.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderRepository orderRepository;

    public OrderItemServiceImpl(OrderLineItemRepository orderLineItemRepository, OrderRepository orderRepository) {
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderItemResponse create(OrderItemRequest orderItemRequest) {
        log.info("OrderItem create: {}", orderItemRequest);
        OrderItem orderItem = OrderItemMapper.toModel(orderItemRequest);
        if(orderItem.getOrder()!=null){
            Order order = orderRepository.findById(orderItem.getOrder().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Order line item with id " + orderItem.getId() + " not found"));
            orderItem.setOrder(order);
        }
        OrderItem savedOrderLineItem = orderLineItemRepository.save(orderItem);
        return OrderItemMapper.toResponse(savedOrderLineItem);
    }

    @Override
    public OrderItemResponse getById(Long id) {
        log.info("OrderItem get by id: {}", id);
        OrderItem orderItem = orderLineItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order line item with id " + id + " not found"));
        return OrderItemMapper.toResponse(orderItem);
    }

    @Override
    public List<OrderItemResponse> getAll() {
        log.info("OrderItem get all");
        List<OrderItem> orderItemList = orderLineItemRepository.findAll();
        return orderItemList.stream().map(OrderItemMapper::toResponse).toList();
    }

    @Override
    public OrderItemResponse update(Long id, OrderItemRequest orderItemRequest) {
        log.info("OrderItem update by id: {}, OrderItemRequest: {}", id, orderItemRequest);
        OrderItem updatedOrderItem = OrderItemMapper.toModel(orderItemRequest);
        updatedOrderItem.setId(id);
        OrderItem savedOrderLineItem = orderLineItemRepository.save(updatedOrderItem);
        return OrderItemMapper.toResponse(savedOrderLineItem);
    }

    @Override
    public void delete(Long id) {
        log.info("OrderItem delete by id: {}", id);
        if (!orderLineItemRepository.existsById(id)) {
            throw new EntityNotFoundException("Order line item with id " + id + " not found");
        }
        orderLineItemRepository.deleteById(id);
    }
}
