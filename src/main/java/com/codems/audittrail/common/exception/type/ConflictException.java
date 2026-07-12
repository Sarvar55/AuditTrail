package com.codems.audittrail.common.exception.type;

import org.springframework.http.HttpStatus;

public class ConflictException extends BaseException {

    public ConflictException() {
        super("EMAIL_ALREADY_EXISTS", "Email already exists", HttpStatus.CONFLICT);
    }
}
