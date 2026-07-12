package com.codems.audittrail.domain.auth.dto;

import com.codems.audittrail.domain.user.model.Role;

public record AuthResponse(
        String accessToken,
        Long userId,
        String email,
        Role role
) {
}
