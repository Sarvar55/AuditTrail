package com.codems.audittrail.common.exception.type;

import com.codems.audittrail.common.exception.model.FieldErrorResponse;

import java.util.List;

import org.springframework.http.HttpStatus;

public class ValidationException extends BaseException {

    private final List<FieldErrorResponse> fieldErrors;

    public ValidationException(List<FieldErrorResponse> fieldErrors) {
        super("VALIDATION_ERROR", "Validation failed", HttpStatus.BAD_REQUEST);
        this.fieldErrors = fieldErrors;
    }

    public List<FieldErrorResponse> getFieldErrors() {
        return fieldErrors;
    }
}
