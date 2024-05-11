package com.maksym.orderservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExceptionPayload {
    private Object errorMessage;
    private String documentationUri;
}
