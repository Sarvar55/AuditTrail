package com.codems.audittrail.domain.auth.dto;

import com.codems.audittrail.domain.user.model.Role;

public record RegisterResponse(
        Long userId,
        String email,
        Role role
) {
}
