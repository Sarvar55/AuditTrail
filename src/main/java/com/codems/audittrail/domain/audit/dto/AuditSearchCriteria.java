package com.codems.audittrail.domain.audit.dto;

import com.codems.audittrail.domain.audit.model.AuditAction;

import java.time.Instant;

public record AuditSearchCriteria(
        Long actorId,
        AuditAction actionType,
        String resourceType,
        String resourceId,
        Instant from,
        Instant to
) {
}
