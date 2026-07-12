package com.codems.audittrail.domain.user.service;

import com.codems.audittrail.common.exception.type.ResourceNotFoundException;
import com.codems.audittrail.domain.audit.annotation.Auditable;
import com.codems.audittrail.domain.audit.model.AuditAction;
import com.codems.audittrail.domain.audit.model.AuditResourceType;
import com.codems.audittrail.domain.user.dto.UpdateProfileRequest;
import com.codems.audittrail.domain.user.dto.UserResponse;
import com.codems.audittrail.domain.user.mapper.UserMapper;
import com.codems.audittrail.domain.user.model.Role;
import com.codems.audittrail.domain.user.model.User;
import com.codems.audittrail.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Auditable(action = AuditAction.PROFILE_UPDATED, resourceType = AuditResourceType.USER, resourceId = "#userId")
    public UserResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = findUser(userId);
        user.updateProfile(request.fullName().trim());
        return userMapper.toResponse(user);
    }

    @Transactional
    @Auditable(action = AuditAction.USER_ROLE_CHANGED, resourceType = AuditResourceType.USER, resourceId = "#userId")
    public UserResponse changeRole(Long userId, Role role) {
        User user = findUser(userId);
        user.changeRole(role);
        return userMapper.toResponse(user);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }
}
