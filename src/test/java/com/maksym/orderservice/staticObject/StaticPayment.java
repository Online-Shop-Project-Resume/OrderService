package com.maksym.orderservice.staticObject;

import com.maksym.orderservice.dto.PaymentRequest;
import com.maksym.orderservice.dto.PaymentResponse;
import com.maksym.orderservice.model.Payment;

import java.time.LocalDateTime;

public class StaticPayment {
    public static Payment payment(){
        Payment payment = new Payment();
        payment.setPaymentDate(LocalDateTime.now());
        payment.setId(1L);
        payment.setQuantity(10);
        payment.setOrderId(1L);
        return payment;
    }

    public static PaymentRequest paymentRequest(){
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setQuantity(10);
        paymentRequest.setOrderId(1L);
        return paymentRequest;
    }

    public static PaymentResponse paymentResponse(){
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPaymentDate(LocalDateTime.now());
        paymentResponse.setId(1L);
        paymentResponse.setQuantity(10);
        paymentResponse.setOrderId(1L);
        return paymentResponse;
    }
}
