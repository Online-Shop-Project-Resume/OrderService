package com.maksym.orderservice.dtoMapper;

import com.maksym.orderservice.dto.PaymentRequest;
import com.maksym.orderservice.dto.PaymentResponse;
import com.maksym.orderservice.model.Payment;

import java.time.LocalDateTime;
import java.util.Date;

public class PaymentMapper {
    public static Payment toModel(PaymentRequest paymentRequest){
        Payment payment = new Payment();
        payment.setPaymentDate(LocalDateTime.now());
        payment.setTotal(paymentRequest.getTotal());
        payment.setQuantity(paymentRequest.getQuantity());
        payment.setOrderId(paymentRequest.getOrderId());

        return payment;
    }

    public static PaymentResponse toResponse(Payment payment){
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setId(payment.getId());
        paymentResponse.setPaymentDate(payment.getPaymentDate());
        paymentResponse.setTotal(payment.getTotal());
        paymentResponse.setQuantity(payment.getQuantity());
        paymentResponse.setOrderId(payment.getOrderId());

        return paymentResponse;
    }
}
