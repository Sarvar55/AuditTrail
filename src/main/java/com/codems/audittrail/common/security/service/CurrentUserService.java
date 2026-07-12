package com.codems.audittrail.common.security.service;

import com.codems.audittrail.common.security.model.SecurityUser;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrentUserService {

    public SecurityUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof SecurityUser user)) {
            throw new AuthenticationCredentialsNotFoundException("Authenticated user not found");
        }
        return user;
    }

    public Long getCurrentUserId() {
        return getCurrentUser().id();
    }

    public Optional<Long> findCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser user) {
            return Optional.of(user.id());
        }
        return Optional.empty();
    }
}
