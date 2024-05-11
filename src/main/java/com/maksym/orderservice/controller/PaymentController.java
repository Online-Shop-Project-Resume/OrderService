package com.maksym.orderservice.controller;

import com.maksym.orderservice.dto.mapper.PaymentDtoMapper;
import com.maksym.orderservice.dto.request.PaymentDtoRequest;
import com.maksym.orderservice.dto.response.PaymentDtoResponse;
import com.maksym.orderservice.model.Payment;
import com.maksym.orderservice.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @Operation(summary = "Create an payment", description = "Create new payment")
    @ApiResponse(responseCode = "201", description = "Payment saved successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Invalid foreign key that is not found")
    @ApiResponse(responseCode = "503", description = "Database connection failed")
    public ResponseEntity<PaymentDtoResponse> createPayment(@Valid @RequestBody PaymentDtoRequest paymentDtoRequest) {
        Payment payment = PaymentDtoMapper.toModel(paymentDtoRequest);
        payment = paymentService.create(payment);
        return new ResponseEntity<>(PaymentDtoMapper.toResponse(payment), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Payment", description = "Get Payment By Id")
    @ApiResponse(responseCode = "200", description = "Payment Get successfully")
    @ApiResponse(responseCode = "404", description = "Payment with such an Id not found")
    public ResponseEntity<PaymentDtoResponse> getPaymentById(@PathVariable("id") Long id) {
        Payment payment = paymentService.getById(id);
        return new ResponseEntity<>(PaymentDtoMapper.toResponse(payment), HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Get All Payment", description = "Get All Payment")
    @ApiResponse(responseCode = "200", description = "Payment Get All successfully")
    @ApiResponse(responseCode = "404", description = "No records with Payment have been found")
    public ResponseEntity<Page<PaymentDtoResponse>> getAllPayment(Pageable pageable) {
        Page<Payment> paymentPage = paymentService.getAll(pageable);
        return new ResponseEntity<>(paymentPage.map(PaymentDtoMapper::toResponse), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an payment", description = "Update an payment by Id and new Payment")
    @ApiResponse(responseCode = "201", description = "Payment updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Payment with such an Id not found or invalid foreign key that is not found")
    public ResponseEntity<PaymentDtoResponse> updatePayment(@PathVariable("id") Long id, @Valid @RequestBody PaymentDtoRequest paymentDtoRequest) {
        Payment payment = PaymentDtoMapper.toModel(paymentDtoRequest);
        payment = paymentService.updateById(id, payment);
        return new ResponseEntity<>(PaymentDtoMapper.toResponse(payment), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an payment", description = "Delete an payment by id")
    @ApiResponse(responseCode = "204", description = "Payment deleted successfully")
    public ResponseEntity<Boolean> deletePayment(@PathVariable("id") Long id) {
        return new ResponseEntity<>(paymentService.deleteById(id), HttpStatus.NO_CONTENT);
    }
}