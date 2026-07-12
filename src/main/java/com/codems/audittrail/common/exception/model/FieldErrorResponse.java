package com.codems.audittrail.common.exception.model;

public record FieldErrorResponse(
        String field,
        String message
) {
}
