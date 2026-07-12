package com.codems.audittrail.domain.audit.controller;

import com.codems.audittrail.domain.audit.dto.AuditEventResponse;
import com.codems.audittrail.domain.audit.dto.AuditSearchCriteria;
import com.codems.audittrail.domain.audit.service.AuditEventService;
import com.codems.audittrail.common.base.BaseResponse;
import com.codems.audittrail.common.base.PageableResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/audit-events/admin")
@RequiredArgsConstructor
public class AuditEventController {

    private final AuditEventService service;

    @GetMapping
    public BaseResponse<PageableResponse<AuditEventResponse>> search(
            @ParameterObject AuditSearchCriteria criteria,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return BaseResponse.success(service.search(criteria, pageable));
    }
}
