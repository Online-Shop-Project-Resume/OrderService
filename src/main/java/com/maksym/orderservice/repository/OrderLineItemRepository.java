package com.maksym.orderservice.repository;


import com.maksym.orderservice.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemRepository extends JpaRepository<OrderItem, Long> {
}
