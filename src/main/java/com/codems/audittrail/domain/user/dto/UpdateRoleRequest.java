package com.codems.audittrail.domain.user.dto;

import com.codems.audittrail.domain.user.model.Role;
import jakarta.validation.constraints.NotNull;

public record UpdateRoleRequest(
        @NotNull Role role
) {
}
