package com.codems.audittrail.domain.audit.listener;

import com.codems.audittrail.domain.audit.model.AuditAction;
import com.codems.audittrail.domain.audit.service.AuditWriter;
import com.codems.audittrail.domain.audit.support.ClientIpResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationAuditListener {

    private final static String AUTHENTICATION = "AUTHENTICATION";

    private final AuditWriter auditWriter;
    private final ClientIpResolver clientIpResolver;

    @EventListener
    public void onLoginFailure(AbstractAuthenticationFailureEvent event) {
        try {
            auditWriter.write(null, AuditAction.LOGIN_FAILED, AUTHENTICATION, null, currentIp());
        } catch (RuntimeException exception) {
            log.error("Failed login attempt could not be audited", exception);
        }
    }

    private String currentIp() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            return clientIpResolver.resolve(attributes.getRequest());
        }
        return null;
    }
}
