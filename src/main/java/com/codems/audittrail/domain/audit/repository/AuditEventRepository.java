package com.codems.audittrail.domain.audit.repository;

import com.codems.audittrail.domain.audit.model.AuditEvent;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditEventRepository extends org.springframework.data.repository.Repository<AuditEvent, Long>, JpaSpecificationExecutor<AuditEvent> {

    AuditEvent save(AuditEvent event);
    java.util.Optional<AuditEvent> findById(Long id);
}
