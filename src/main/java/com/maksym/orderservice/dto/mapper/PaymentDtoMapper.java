package com.maksym.orderservice.dto.mapper;

import com.maksym.orderservice.model.Payment;
import com.maksym.orderservice.dto.request.PaymentDtoRequest;
import com.maksym.orderservice.dto.response.PaymentDtoResponse;

public class PaymentDtoMapper {

    public static Payment toModel(PaymentDtoRequest request) {
        Payment model = new Payment();

        model.setTotal(request.getTotal());
        model.setOrderId(request.getOrderId());
        model.setQuantity(request.getQuantity());

        return model;
    }

    public static PaymentDtoResponse toResponse(Payment model) {
        PaymentDtoResponse response = new PaymentDtoResponse();

        response.setId(model.getId());
        response.setTotal(model.getTotal());
        response.setOrderId(model.getOrderId());
        response.setPaymentDate(model.getPaymentDate());
        response.setQuantity(model.getQuantity());

        return response;
    }

    private PaymentDtoMapper() {}

}
