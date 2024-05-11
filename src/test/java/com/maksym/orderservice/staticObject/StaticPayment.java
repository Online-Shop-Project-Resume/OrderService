package com.maksym.orderservice.staticObject;

import com.maksym.orderservice.dto.request.PaymentDtoRequest;
import com.maksym.orderservice.dto.response.PaymentDtoResponse;
import com.maksym.orderservice.model.Payment;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class StaticPayment {

    public static final Long ID = 1L;

    public static Payment payment1() {
        Payment model = new Payment();
        model.setId(ID);
        model.setTotal(new BigDecimal(10));
        model.setOrderId(1L);
        model.setPaymentDate(LocalDateTime.MIN);
        model.setQuantity(1);
        return model;
    }

    public static Payment payment2() {
        Payment model = new Payment();
        model.setId(ID);
        model.setTotal(new BigDecimal(20));
        model.setOrderId(2L);
        model.setPaymentDate(LocalDateTime.MIN);
        model.setQuantity(2);
        return model;
    }

    public static PaymentDtoRequest paymentDtoRequest1() {
        PaymentDtoRequest dtoRequest = new PaymentDtoRequest();
        dtoRequest.setTotal(new BigDecimal(10));
        dtoRequest.setOrderId(1L);
        dtoRequest.setQuantity(1);
        return dtoRequest;
    }

    public static PaymentDtoResponse paymentDtoResponse1() {
        PaymentDtoResponse dtoResponse = new PaymentDtoResponse();
        dtoResponse.setId(ID);
        dtoResponse.setTotal(new BigDecimal(10));
        dtoResponse.setOrderId(1L);
        dtoResponse.setPaymentDate(LocalDateTime.MIN);
        dtoResponse.setQuantity(1);
        return dtoResponse;
    }

    public static PaymentDtoResponse paymentDtoResponse2() {
        PaymentDtoResponse dtoResponse = new PaymentDtoResponse();
        dtoResponse.setId(ID);
        dtoResponse.setTotal(new BigDecimal(20));
        dtoResponse.setOrderId(2L);
        dtoResponse.setPaymentDate(LocalDateTime.MIN);
        dtoResponse.setQuantity(2);
        return dtoResponse;
    }
}
