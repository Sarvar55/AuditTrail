package com.codems.audittrail.common.exception.type;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(String resourceName, Object resourceId) {
        super(
                "RESOURCE_NOT_FOUND",
                "%s with id '%s' was not found".formatted(resourceName, resourceId),
                HttpStatus.NOT_FOUND
        );
    }
}
