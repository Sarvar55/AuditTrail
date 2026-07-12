package com.codems.audittrail.domain.audit.service;

import com.codems.audittrail.domain.audit.dto.AuditEventResponse;
import com.codems.audittrail.domain.audit.dto.AuditSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;

@Service
@RequiredArgsConstructor
public class AuditCsvExportService {

    private static final String[] HEADERS = {
            "ID", "Actor ID", "Action", "Resource Type", "Resource ID", "IP Address", "Created At"
    };

    private final AuditEventService auditEventService;

    public String export(AuditSearchCriteria criteria) {
        StringWriter writer = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT.builder().setHeader(HEADERS).get();

        try (CSVPrinter printer = new CSVPrinter(writer, format)) {
            for (AuditEventResponse event : auditEventService.findForExport(criteria)) {
                printer.printRecord(
                        event.id(),
                        event.actorId(),
                        event.actionType(),
                        event.resourceType(),
                        event.resourceId(),
                        event.ipAddress(),
                        event.createdAt()
                );
            }
            return writer.toString();
        } catch (IOException exception) {
            throw new IllegalStateException("CSV export failed", exception);
        }
    }
}
