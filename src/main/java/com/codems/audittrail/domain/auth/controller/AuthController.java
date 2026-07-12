package com.codems.audittrail.domain.auth.controller;

import com.codems.audittrail.common.base.BaseResponse;
import com.codems.audittrail.domain.auth.dto.AuthResponse;
import com.codems.audittrail.domain.auth.dto.LoginRequest;
import com.codems.audittrail.domain.auth.dto.RegisterRequest;
import com.codems.audittrail.domain.auth.dto.RegisterResponse;
import com.codems.audittrail.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;

@RestController
@RequestMapping("/auth")
@SecurityRequirements
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<BaseResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.success(response, HttpStatus.CREATED, "Registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(BaseResponse.success(authService.login(request), HttpStatus.OK, "Logged in successfully"));
    }
}
