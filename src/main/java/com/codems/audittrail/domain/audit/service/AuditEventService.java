package com.codems.audittrail.domain.audit.service;

import com.codems.audittrail.common.base.PageableResponse;
import com.codems.audittrail.domain.audit.dto.AuditEventResponse;
import com.codems.audittrail.domain.audit.dto.AuditSearchCriteria;
import com.codems.audittrail.domain.audit.mapper.AuditEventMapper;
import com.codems.audittrail.domain.audit.repository.AuditEventRepository;
import com.codems.audittrail.domain.audit.repository.AuditEventSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuditEventService {

    private final AuditEventRepository repository;
    private final AuditEventMapper mapper;


    public PageableResponse<AuditEventResponse> search(AuditSearchCriteria criteria, Pageable pageable) {
        var page = repository.findAll(AuditEventSpecification.withFilters(criteria), pageable)
                .map(mapper::toResponse);

        return PageableResponse.from(page);
    }

    public List<AuditEventResponse> findForExport(AuditSearchCriteria criteria) {
        return repository.findAll(
                        AuditEventSpecification.withFilters(criteria),
                        Sort.by(Sort.Direction.DESC, "createdAt")
                ).stream()
                .map(mapper::toResponse)
                .toList();
    }
}
