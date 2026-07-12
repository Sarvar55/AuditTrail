package com.codems.audittrail.common.exception.type;

import org.springframework.http.HttpStatus;

public class CompromisedPasswordException extends BaseException {

    public CompromisedPasswordException() {
        super(
                "COMPROMISED_PASSWORD",
                "The password has appeared in a known data breach and cannot be used",
                HttpStatus.BAD_REQUEST
        );
    }
}
