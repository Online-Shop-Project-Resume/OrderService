package com.maksym.orderservice.service;

import com.maksym.orderservice.dto.OrderRequest;
import com.maksym.orderservice.dto.OrderResponse;
import com.maksym.orderservice.dtoMapper.OrderMapper;
import com.maksym.orderservice.event.NotificationEvent;
import com.maksym.orderservice.model.Order;
import com.maksym.orderservice.repository.OrderRepository;
import com.maksym.orderservice.util.enums.OrderStatus;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
@Transactional
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    public OrderServiceImpl(OrderRepository orderRepository, KafkaTemplate kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
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

    @Override
    public boolean updateStatusOrder(Long id, OrderStatus orderStatus) {
        log.info("Order update Status to {} by id: {}", orderStatus, id);
        Order order = orderRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Order with id: "+id+" is not found"));
        order.setStatus(orderStatus);
        orderRepository.save(order);

        if(orderStatus==OrderStatus.CANCELED || orderStatus==OrderStatus.SHIPPED || orderStatus==OrderStatus.DELIVERED)
            kafkaTemplate.send("notificationTopic", notification(order));
        return true;
    }

    public NotificationEvent notification (Order order){
        NotificationEvent notificationEvent = new NotificationEvent();
        if(order.getStatus()== OrderStatus.SHIPPED){
            notificationEvent.setTitle("Your Order Has Been Shipped");
            notificationEvent.setDescription("We are excited to inform you that your order: " + order.getOrderNumber() + " has been shipped! Your package is now on its way to you and should arrive shortly.\n" +
                    "\n" +
                    "Thank you for shopping with us!");
        }else if(order.getStatus()== OrderStatus.DELIVERED){
            notificationEvent.setTitle("Your Order Has Been Delivered");
            notificationEvent.setDescription("Great news! Your order has been successfully delivered to your address. We hope you are delighted with your purchase.\n" +
                    "\n" +
                    "Thank you for choosing Online Shop!");
        }else if(order.getStatus()== OrderStatus.CANCELED){
            notificationEvent.setTitle("Order Cancellation Confirmation");
            notificationEvent.setDescription("We regret to inform you that your order has been canceled. If you have any questions or concerns, please feel free to contact us.");
        }
        return notificationEvent;
    }
}
