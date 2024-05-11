package com.maksym.orderservice.service;

import com.maksym.orderservice.exception.EntityNotFoundException;
import com.maksym.orderservice.model.Payment;
import com.maksym.orderservice.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment create(Payment payment) {
        log.info("Payment create: {}", payment);

        return paymentRepository.save(payment);
    }

    public Payment getById(Long id) {
        log.info("Payment get by id: {}", id);
        return paymentRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Payment with id: " + id + " does not exist"));
    }

    public Page<Payment> getAll(Pageable pageable) {
        log.info("Payment get all: {}", pageable);
        return paymentRepository.findAll(pageable);
    }

    public Payment updateById(Long id, Payment payment) {
        getById(id);
        payment.setId(id);

        log.info("Payment update by id: {}", payment);
        return paymentRepository.save(payment);
    }

    public Boolean deleteById(Long id) {
        log.info("Payment delete by id: {}", id);
        paymentRepository.deleteById(id);
        return true;
    }
}
