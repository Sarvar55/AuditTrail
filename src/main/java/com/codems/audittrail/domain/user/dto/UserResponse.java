package com.codems.audittrail.domain.user.dto;

import com.codems.audittrail.domain.user.model.Role;

public record UserResponse(
        Long id,
        String fullName,
        String email,
        Role role
) {
}
