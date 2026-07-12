package com.codems.audittrail.domain.audit.repository;

import com.codems.audittrail.domain.audit.dto.AuditSearchCriteria;
import com.codems.audittrail.domain.audit.model.AuditEvent;
import org.springframework.data.jpa.domain.Specification;

public final class AuditEventSpecification {

    private AuditEventSpecification() {
    }

    public static Specification<AuditEvent> withFilters(AuditSearchCriteria criteria) {
        return Specification.allOf(
                equal("actorId", criteria.actorId()),
                equal("actionType", criteria.actionType()),
                equal("resourceType", criteria.resourceType()),
                equal("resourceId", criteria.resourceId()),
                createdAfter(criteria.from()),
                createdBefore(criteria.to())
        );
    }

    private static Specification<AuditEvent> equal(String field, Object value) {
        return value == null
                ? Specification.unrestricted()
                : (root, query, builder) -> builder.equal(root.get(field), value);
    }

    private static Specification<AuditEvent> createdAfter(java.time.Instant from) {
        return from == null ? Specification.unrestricted()
                : (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("createdAt"), from);
    }

    private static Specification<AuditEvent> createdBefore(java.time.Instant to) {
        return to == null ? Specification.unrestricted()
                : (root, query, builder) -> builder.lessThanOrEqualTo(root.get("createdAt"), to);
    }
}
