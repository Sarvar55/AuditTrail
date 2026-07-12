package com.codems.audittrail.domain.task.dto;

import java.time.Instant;

public record TaskResponse(
        Long id,
        String title,
        String description,
        Long ownerId,
        Instant createdAt,
        Instant updatedAt
) {
}
