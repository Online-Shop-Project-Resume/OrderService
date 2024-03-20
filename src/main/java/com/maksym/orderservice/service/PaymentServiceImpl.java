package com.maksym.orderservice.service;

import com.maksym.orderservice.dto.PaymentRequest;
import com.maksym.orderservice.dto.PaymentResponse;
import com.maksym.orderservice.dtoMapper.PaymentMapper;
import com.maksym.orderservice.model.Payment;
import com.maksym.orderservice.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentResponse create(PaymentRequest paymentRequest) {
        log.info("Payment create: {}", paymentRequest);
        Payment payment = PaymentMapper.toModel(paymentRequest);
        Payment savedPayment = paymentRepository.save(payment);
        return PaymentMapper.toResponse(savedPayment);
    }

    @Override
    public PaymentResponse getById(Long id) {
        log.info("Payment get by id: {}", id);
        Payment payment = paymentRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Payment with id: "+id+" is not found"));
        return PaymentMapper.toResponse(payment);
    }

    @Override
    public List<PaymentResponse> getAll() {
        log.info("Payment get all");
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream().map(PaymentMapper::toResponse).toList();
    }

    @Override
    public PaymentResponse update(Long id, PaymentRequest paymentRequest) {
        log.info("Payment update by id: {}, PaymentRequest: {id}", paymentRequest);
        if(!paymentRepository.existsById(id)) throw new EntityNotFoundException("Payment with id: "+id+" is not found");
        Payment updatedPayment = PaymentMapper.toModel(paymentRequest);
        updatedPayment.setId(id);
        updatedPayment = paymentRepository.save(updatedPayment);
        return PaymentMapper.toResponse(updatedPayment);
    }

    @Override
    public void delete(Long id) {
        log.info("Payment delete by id: {}", id);
        paymentRepository.deleteById(id);
    }
}
