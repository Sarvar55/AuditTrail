package com.codems.audittrail.domain.user.mapper;

import com.codems.audittrail.domain.user.dto.UserResponse;
import com.codems.audittrail.domain.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserResponse toResponse(User user);
}
