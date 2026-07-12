package com.codems.audittrail.domain.user.controller;

import com.codems.audittrail.common.base.BaseResponse;
import com.codems.audittrail.domain.user.dto.UpdateRoleRequest;
import com.codems.audittrail.domain.user.dto.UserResponse;
import com.codems.audittrail.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/admin")
@Tag(name = "Admin users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @PutMapping("/{userId}/role")
    @Operation(summary = "Change user role", description = "Requires the ADMIN role")
    public ResponseEntity<BaseResponse<UserResponse>> changeRole(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateRoleRequest request
    ) {
        return ResponseEntity.ok(BaseResponse.success(
                userService.changeRole(userId, request.role()),
                org.springframework.http.HttpStatus.OK,
                "User role updated successfully"
        ));
    }
}
