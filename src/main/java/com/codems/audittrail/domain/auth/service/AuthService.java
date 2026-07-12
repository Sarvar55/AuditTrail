package com.codems.audittrail.domain.auth.service;

import com.codems.audittrail.common.exception.type.CompromisedPasswordException;
import com.codems.audittrail.common.exception.type.ConflictException;
import com.codems.audittrail.common.exception.type.InvalidCredentialsException;
import com.codems.audittrail.common.security.model.SecurityUser;
import com.codems.audittrail.common.security.service.JwtService;
import com.codems.audittrail.domain.auth.dto.AuthResponse;
import com.codems.audittrail.domain.auth.dto.LoginRequest;
import com.codems.audittrail.domain.auth.dto.RegisterRequest;
import com.codems.audittrail.domain.auth.dto.RegisterResponse;
import com.codems.audittrail.domain.user.model.Role;
import com.codems.audittrail.domain.user.model.User;
import com.codems.audittrail.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid email or password";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CompromisedPasswordChecker compromisedPasswordChecker;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.email());
        validateEmailAvailability(email);
        validatePassword(request.password());

        User user = createUser(request, email);
        User savedUser = userRepository.save(user);

        return new RegisterResponse(savedUser.getId(), savedUser.getEmail(), savedUser.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        SecurityUser user = authenticate(request);
        String accessToken = jwtService.generateToken(user);
        return new AuthResponse(accessToken, user.id(), user.email(), user.role());
    }

    private SecurityUser authenticate(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(
                            normalizeEmail(request.email()),
                            request.password()
                    )
            );
            return (SecurityUser) authentication.getPrincipal();
        } catch (BadCredentialsException exception) {
            throw new InvalidCredentialsException(INVALID_CREDENTIALS_MESSAGE);
        }
    }

    private void validateEmailAvailability(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException();
        }
    }

    private void validatePassword(String password) {
        if (compromisedPasswordChecker.check(password).isCompromised()) {
            throw new CompromisedPasswordException();
        }
    }

    private User createUser(RegisterRequest request, String normalizedEmail) {
        return User.of(
                request.fullName(),
                normalizedEmail,
                passwordEncoder.encode(request.password()),
                Role.USER
        );
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
