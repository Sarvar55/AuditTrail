package com.codems.audittrail.domain.audit.controller;

import com.codems.audittrail.common.base.BaseResponse;
import com.codems.audittrail.common.base.PageableResponse;
import com.codems.audittrail.domain.audit.dto.AuditEventResponse;
import com.codems.audittrail.domain.audit.dto.AuditSearchCriteria;
import com.codems.audittrail.domain.audit.service.AuditCsvExportService;
import com.codems.audittrail.domain.audit.service.AuditEventService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/audit-events/admin")
@RequiredArgsConstructor
public class AuditEventController {

    private final AuditEventService service;
    private final AuditCsvExportService csvExportService;

    @GetMapping
    public BaseResponse<PageableResponse<AuditEventResponse>> search(
            @ParameterObject AuditSearchCriteria criteria, @ParameterObject @PageableDefault Pageable pageable) {
        return BaseResponse.success(service.search(criteria, pageable));
    }

    @GetMapping(value = "/export", produces = "text/csv")
    public ResponseEntity<String> export(@ParameterObject AuditSearchCriteria criteria) {
        return ResponseEntity.ok()
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"audit-events.csv\"")
                .body(csvExportService.export(criteria));
    }
}
