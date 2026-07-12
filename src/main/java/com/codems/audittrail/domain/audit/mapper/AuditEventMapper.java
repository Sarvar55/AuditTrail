package com.codems.audittrail.domain.audit.mapper;

import com.codems.audittrail.domain.audit.dto.AuditEventResponse;
import com.codems.audittrail.domain.audit.model.AuditEvent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuditEventMapper {
    AuditEventResponse toResponse(AuditEvent event);
}
