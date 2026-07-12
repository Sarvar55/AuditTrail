package com.codems.audittrail.domain.audit.service;

import com.codems.audittrail.domain.audit.model.AuditAction;
import com.codems.audittrail.domain.audit.model.AuditEvent;
import com.codems.audittrail.domain.audit.model.AuditResourceType;
import com.codems.audittrail.domain.audit.repository.AuditEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditWriter {

    private final AuditEventRepository auditEventRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void write(Long actorId, AuditAction action, AuditResourceType resourceType,
                      String resourceId, String ipAddress) {
        auditEventRepository.save(AuditEvent.create(actorId, action, resourceType, resourceId, ipAddress));
    }
}
