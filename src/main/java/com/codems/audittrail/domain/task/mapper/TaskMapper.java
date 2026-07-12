package com.codems.audittrail.domain.task.mapper;

import com.codems.audittrail.domain.task.dto.TaskResponse;
import com.codems.audittrail.domain.task.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {
    TaskResponse toResponse(Task task);
}
