package com.maksym.orderservice.service;


import com.maksym.orderservice.exception.EntityNotFoundException;
import com.maksym.orderservice.model.Payment;
import com.maksym.orderservice.repository.PaymentRepository;
import com.maksym.orderservice.staticObject.StaticPayment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;
    @InjectMocks
    private PaymentService paymentService;
    private final Payment payment = StaticPayment.payment1();
    private final Payment payment2 = StaticPayment.payment2();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
	    when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment createdPayment = paymentService.create(payment);

        assertNotNull(createdPayment);
        assertEquals(payment, createdPayment);
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testCreate_DataAccessException() {
        when(paymentRepository.findById(StaticPayment.ID)).thenThrow(new DataAccessException("Database connection failed") {
        });

        RuntimeException exception = assertThrows(RuntimeException.class, () -> paymentService.getById(StaticPayment.ID));

        assertNotNull(exception);
        assertEquals("Database connection failed", exception.getMessage());
        verify(paymentRepository, times(1)).findById(StaticPayment.ID);
    }

    @Test
    void testGetAll() {
        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(payment);
        paymentList.add(payment2);
        Page<Payment> paymentPage = new PageImpl<>(paymentList);
        Pageable pageable = Pageable.unpaged();
        when(paymentRepository.findAll(pageable)).thenReturn(paymentPage);

        Page<Payment> result = paymentService.getAll(pageable);

        assertEquals(paymentList.size(), result.getSize());
        assertEquals(payment, result.getContent().get(0));
        assertEquals(payment2, result.getContent().get(1));
    }

    @Test
    void testGetAll_AnyException() {
        when(paymentRepository.findAll(any(Pageable.class))).thenThrow(new DataAccessException("Database connection failed") {});

        Pageable pageable = Pageable.unpaged();
        RuntimeException exception = assertThrows(DataAccessException.class, () -> paymentService.getAll(pageable));

        assertNotNull(exception);
        assertEquals("Database connection failed", exception.getMessage());
        verify(paymentRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testUpdate_Success() {
	    Payment existingPayment = StaticPayment.payment1();
        Payment updatedPayment = StaticPayment.payment2();
	    when(paymentRepository.findById(StaticPayment.ID)).thenReturn(java.util.Optional.of(existingPayment));
        when(paymentRepository.save(updatedPayment)).thenReturn(updatedPayment);

        Payment result = paymentService.updateById(StaticPayment.ID, updatedPayment);

        assertEquals(updatedPayment, result);
        verify(paymentRepository, times(1)).findById(StaticPayment.ID);
        verify(paymentRepository, times(1)).save(updatedPayment);
    }


    @Test
    void testUpdateById_EntityNotFoundException() {
        Payment updatedPayment = StaticPayment.payment1();
        when(paymentRepository.findById(StaticPayment.ID)).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> paymentService.updateById(StaticPayment.ID, updatedPayment));
        verify(paymentRepository, times(1)).findById(StaticPayment.ID);
        verify(paymentRepository, never()).save(updatedPayment);
    }

    @Test
    void testUpdateById_AnyException() {
        Payment existingPayment = StaticPayment.payment1();
        Payment updatedPayment = StaticPayment.payment2();
        when(paymentRepository.findById(StaticPayment.ID)).thenReturn(java.util.Optional.of(existingPayment));
	    when(paymentRepository.save(updatedPayment)).thenThrow(new DataAccessException("Database connection failed") {
        });

        RuntimeException exception = assertThrows(RuntimeException.class, () -> paymentService.updateById(StaticPayment.ID, updatedPayment));

        assertNotNull(exception);
        assertEquals("Database connection failed", exception.getMessage());
        verify(paymentRepository, times(1)).save(updatedPayment);
    }

    @Test
    void testDeleteById_Success() {
        boolean result = paymentService.deleteById(StaticPayment.ID);

        verify(paymentRepository).deleteById(StaticPayment.ID);
        assertTrue(result);
    }

    @Test
    void testDeleteById_AnyException() {
        doThrow(new DataAccessException("Database connection failed") {}).when(paymentRepository).deleteById(StaticPayment.ID);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> paymentService.deleteById(StaticPayment.ID));

        assertNotNull(exception);
        assertEquals("Database connection failed", exception.getMessage());
        verify(paymentRepository, times(1)).deleteById(StaticPayment.ID);
    }
}