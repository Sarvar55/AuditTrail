package com.codems.audittrail.domain.audit.support;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class ClientIpResolver {
    public String resolve(HttpServletRequest request) {
        if (request == null) return null;

        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) return forwarded.split(",", 2)[0].trim();

        String real = request.getHeader("X-Real-IP");

        return real != null && !real.isBlank() ? real.trim() : request.getRemoteAddr();
    }
}
