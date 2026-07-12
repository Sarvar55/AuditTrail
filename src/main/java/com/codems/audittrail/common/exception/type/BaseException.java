package com.codems.audittrail.common.exception.type;

import com.codems.audittrail.common.exception.model.ErrorResponse;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException {

    private final String code;
    private final HttpStatus status;

    protected BaseException(String code, String message, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
