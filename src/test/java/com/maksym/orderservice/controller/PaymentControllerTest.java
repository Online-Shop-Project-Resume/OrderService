package com.maksym.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maksym.orderservice.dto.request.PaymentDtoRequest;
import com.maksym.orderservice.dto.response.PaymentDtoResponse;
import com.maksym.orderservice.model.Payment;
import com.maksym.orderservice.exception.EntityNotFoundException;
import com.maksym.orderservice.service.PaymentService;
import com.maksym.orderservice.staticObject.StaticPayment;
import com.maksym.orderservice.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    private final String DOCUMENTATION_URI = "http://swagger_documentation";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private final PaymentDtoRequest paymentRequest = StaticPayment.paymentDtoRequest1();
    private final Payment paymentModel = StaticPayment.payment1(); 
    private final PaymentDtoResponse paymentResponse = StaticPayment.paymentDtoResponse1();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        PaymentController paymentController = new PaymentController(paymentService);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController)
                .setControllerAdvice(new GlobalExceptionHandler(DOCUMENTATION_URI))
                .build();
    }

    @Test
    void testCreate_Success_ShouldReturnCreated() throws Exception {
        when(paymentService.create(any(Payment.class))).thenReturn(paymentModel);

        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(paymentResponse.getId()))
                .andExpect(jsonPath("$.total").value(paymentResponse.getTotal()))
                .andExpect(jsonPath("$.orderId").value(paymentResponse.getOrderId()))
                .andExpect(jsonPath("$.paymentDate").value(paymentResponse.getPaymentDate().format(formatter)))
                .andExpect(jsonPath("$.quantity").value(paymentResponse.getQuantity()));
    }

    @Test
    void testCreate_InvalidInput_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreate_EntityNotFoundException_ShouldReturnNotFound() throws Exception {
        when(paymentService.create(any(Payment.class))).thenThrow(new EntityNotFoundException("Payment not found"));

        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Payment not found"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }

    @Test
    void testCreate_AnyException_ShouldReturnBadRequest() throws Exception {
        doThrow(new DataAccessException("Database connection failed") {}).when(paymentService).create(any(Payment.class));

        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Database connection failed"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }

    @Test
    void testGetById_Success_ShouldReturnOk() throws Exception {
        when(paymentService.getById(StaticPayment.ID)).thenReturn(paymentModel);

        mockMvc.perform(get("/api/payment/{id}", StaticPayment.ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(paymentResponse.getId()))
                .andExpect(jsonPath("$.total").value(paymentResponse.getTotal()))
                .andExpect(jsonPath("$.orderId").value(paymentResponse.getOrderId()))
                .andExpect(jsonPath("$.paymentDate").value(paymentResponse.getPaymentDate().format(formatter)))
                .andExpect(jsonPath("$.quantity").value(paymentResponse.getQuantity()));
    }

    @Test
    void testGetById_EntityNotFoundException_ShouldReturnNotFound() throws Exception {
        when(paymentService.getById(any())).thenThrow(new EntityNotFoundException("Payment not found"));

        mockMvc.perform(get("/api/payment/"+StaticPayment.ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Payment not found"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }

    @Test
    void testGetById_AnyException_ShouldReturnBadRequest() throws Exception {
        doThrow(new DataAccessException("Database connection failed") {}).when(paymentService).getById(any());

        mockMvc.perform(get("/api/payment/"+StaticPayment.ID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Database connection failed"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }


    @Test
    void testGetAll_Success_ShouldReturnOk() throws Exception {
        List<Payment> paymentList = Arrays.asList(paymentModel, StaticPayment.payment1());
        Page<Payment> paymentPage = new PageImpl<>(paymentList);
        Pageable pageable = Pageable.unpaged();
        when(paymentService.getAll(pageable)).thenReturn(paymentPage);

        mockMvc.perform(get("/api/payment/"))
                .andExpect(status().isOk())
		        .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id").value(paymentResponse.getId()))
                .andExpect(jsonPath("$.[1].id").value(StaticPayment.paymentDtoResponse2().getId()));
    }

    @Test
    void testGetAll_AnyException_ShouldReturnBadRequest() throws Exception {
        doThrow(new DataAccessException("Database connection failed") {}).when(paymentService).getAll(any(Pageable.class));

        mockMvc.perform(get("/api/payment/"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Database connection failed"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }


    @Test
    void testUpdateById_Success_ShouldReturnOk() throws Exception {
        when(paymentService.updateById(any(), any(Payment.class))).thenReturn(paymentModel);

        mockMvc.perform(put("/api/payment/"+StaticPayment.ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(paymentResponse.getId()))
                .andExpect(jsonPath("$.total").value(paymentResponse.getTotal()))
                .andExpect(jsonPath("$.orderId").value(paymentResponse.getOrderId()))
                .andExpect(jsonPath("$.paymentDate").value(paymentResponse.getPaymentDate().format(formatter)))
                .andExpect(jsonPath("$.quantity").value(paymentResponse.getQuantity()));
    }

    @Test
    void testUpdateById_InvalidInput_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/api/payment/"+StaticPayment.ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateById_EntityNotFoundException_ShouldReturnNotFound() throws Exception {
        when(paymentService.updateById(any(), any(Payment.class))).thenThrow(new EntityNotFoundException("Payment not found"));

        mockMvc.perform(put("/api/payment/"+StaticPayment.ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Payment not found"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }

    @Test
    void testUpdateById_AnyException_ShouldReturnBadRequest() throws Exception {
        doThrow(new DataAccessException("Database connection failed") {}).when(paymentService).updateById(any(), any(Payment.class));

        mockMvc.perform(put("/api/payment/"+StaticPayment.ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Database connection failed"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }

    @Test
    void testDelete_Success_ShouldReturnNoContent() throws Exception {
        when(paymentService.deleteById(StaticPayment.ID)).thenReturn(true);

        mockMvc.perform(delete("/api/payment/"+StaticPayment.ID))
                .andExpect(status().isNoContent());
    }
	
    @Test
    void testDelete_AnyException_ShouldReturnBadRequest() throws Exception {
        doThrow(new DataAccessException("Database connection failed") {}).when(paymentService).deleteById(StaticPayment.ID);

        mockMvc.perform(delete("/api/payment/"+StaticPayment.ID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Database connection failed"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }
}