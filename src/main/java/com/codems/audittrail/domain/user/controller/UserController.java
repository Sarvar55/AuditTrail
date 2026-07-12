package com.codems.audittrail.domain.user.controller;

import com.codems.audittrail.common.base.BaseResponse;
import com.codems.audittrail.common.security.model.SecurityUser;
import com.codems.audittrail.domain.user.dto.UpdateProfileRequest;
import com.codems.audittrail.domain.user.dto.UserResponse;
import com.codems.audittrail.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/me")
@Tag(name = "Profile")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get current profile")
    public ResponseEntity<BaseResponse<UserResponse>> getCurrentUser(@AuthenticationPrincipal SecurityUser user) {

        UserResponse profile = new UserResponse(
                user.id(),
                user.fullName(),
                user.email(),
                user.role()
        );
        return ResponseEntity.ok(BaseResponse.success(profile));
    }

    @PutMapping
    @Operation(summary = "Update current profile")
    public ResponseEntity<BaseResponse<UserResponse>> updateProfile(@Valid @RequestBody UpdateProfileRequest request, @AuthenticationPrincipal SecurityUser user) {
        return ResponseEntity.ok(BaseResponse.success(
                userService.updateProfile(user.id(), request),
                org.springframework.http.HttpStatus.OK,
                "Profile updated successfully"
        ));
    }
}
