package com.maksym.orderservice.service;

import com.maksym.orderservice.dto.PaymentRequest;
import com.maksym.orderservice.dto.PaymentResponse;
import com.maksym.orderservice.model.Payment;
import com.maksym.orderservice.repository.PaymentRepository;
import com.maksym.orderservice.staticObject.StaticPayment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    private final Long id = 1L;
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void testCreate() {
        PaymentRequest paymentRequest = StaticPayment.paymentRequest();
        Payment payment = StaticPayment.payment();
        // Set payment request fields

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentResponse response = paymentService.create(paymentRequest);

        assertEquals(payment.getId(), response.getId());
        // Add more assertions as needed

        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testGetById() {
        Payment payment = StaticPayment.payment();
        when(paymentRepository.findById(id)).thenReturn(Optional.of(payment));

        PaymentResponse response = paymentService.getById(id);

        assertEquals(payment.getId(), response.getId());
        // Add more assertions as needed

        verify(paymentRepository, times(1)).findById(id);
    }

    @Test
    void testGetAll() {
        Payment payment = StaticPayment.payment();
        when(paymentRepository.findAll()).thenReturn(Collections.singletonList(payment));

        List<PaymentResponse> responses = paymentService.getAll();

        assertEquals(1, responses.size());
        assertEquals(payment.getId(), responses.get(0).getId());
        // Add more assertions as needed

        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    void testUpdate() {
        PaymentRequest paymentRequest = StaticPayment.paymentRequest();
        Payment payment = StaticPayment.payment();
        // Set payment request fields

        when(paymentRepository.existsById(id)).thenReturn(true);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentResponse response = paymentService.update(id, paymentRequest);

        assertEquals(payment.getId(), response.getId());
        // Add more assertions as needed

        verify(paymentRepository, times(1)).existsById(id);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testDelete() {
        paymentService.delete(id);

        verify(paymentRepository, times(1)).deleteById(id);
    }
}

