package com.codems.audittrail.domain.audit.dto;

import com.codems.audittrail.domain.audit.model.AuditAction;
import com.codems.audittrail.domain.audit.model.AuditResourceType;

import java.time.Instant;

public record AuditSearchCriteria(
        Long actorId,
        AuditAction actionType,
        AuditResourceType resourceType,
        String resourceId,
        Instant from,
        Instant to
) {
}
