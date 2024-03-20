package com.maksym.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maksym.orderservice.dto.PaymentRequest;
import com.maksym.orderservice.dto.PaymentResponse;
import com.maksym.orderservice.exception.GlobalExceptionHandler;
import com.maksym.orderservice.service.PaymentServiceImpl;
import com.maksym.orderservice.staticObject.StaticPayment;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PaymentControllerTest {

    private final long id = 1L;
    @Mock
    private PaymentServiceImpl paymentService;
    @InjectMocks
    private PaymentController paymentController;
    private MockMvc mockMvc;

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();
    }
    @Test
    void testCreatePayment() throws Exception {
        PaymentRequest request = StaticPayment.paymentRequest();
        PaymentResponse response = StaticPayment.paymentResponse();
        when(paymentService.create(any(PaymentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.quantity").value(response.getQuantity()))
                .andExpect(jsonPath("$.orderId").value(response.getOrderId()));
    }

    @Test
    void testGetPaymentById() throws Exception {
        long id = 1L;
        PaymentResponse response = StaticPayment.paymentResponse();
        when(paymentService.getById(id)).thenReturn(response);

        mockMvc.perform(get("/api/payment/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.quantity").value(response.getQuantity()))
                .andExpect(jsonPath("$.orderId").value(response.getOrderId()));
    }

    @Test
    void testGetAllPayments() throws Exception {
        List<PaymentResponse> responseList = new ArrayList<>();
        // Add some dummy PaymentResponse objects to the list

        when(paymentService.getAll()).thenReturn(responseList);

        mockMvc.perform(get("/api/payment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(responseList.size())));
    }

    @Test
    void testUpdatePayment() throws Exception {
        PaymentRequest request = StaticPayment.paymentRequest();
        PaymentResponse response = StaticPayment.paymentResponse();
        when(paymentService.update(id, request)).thenReturn(response);

        mockMvc.perform(put("/api/payment/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.quantity").value(response.getQuantity()))
                .andExpect(jsonPath("$.orderId").value(response.getOrderId()));
    }

    @Test
    void testDeletePayment() throws Exception {
        mockMvc.perform(delete("/api/payment/{id}", id))
                .andExpect(status().isOk());

        verify(paymentService, times(1)).delete(id);
    }

    @Test
    void testHandleConstraintViolationException() throws Exception {
        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")) // Invalid JSON to trigger ConstraintViolationException
                .andExpect(status().isBadRequest());
    }

    @Test
    void testHandleNotFoundException() throws Exception {
        when(paymentService.getById(id)).thenThrow(new EntityNotFoundException("Payment with id: " + id + " is not found"));

        mockMvc.perform(get("/api/payment/{id}", id))
                .andExpect(status().isNotFound());
    }
}
