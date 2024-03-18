package com.maksym.orderservice.service;

import com.maksym.orderservice.dto.PaymentRequest;
import com.maksym.orderservice.dto.PaymentResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse create(PaymentRequest paymentRequest);
    PaymentResponse getById(Long id);
    List<PaymentResponse> getAll();
    PaymentResponse update(Long id, PaymentRequest paymentRequest);
    void delete(Long id);
}
