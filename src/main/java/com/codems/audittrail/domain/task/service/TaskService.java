package com.codems.audittrail.domain.task.service;

import com.codems.audittrail.common.exception.type.ResourceNotFoundException;
import com.codems.audittrail.common.base.PageableResponse;
import com.codems.audittrail.domain.task.dto.TaskCreateRequest;
import com.codems.audittrail.domain.task.dto.TaskResponse;
import com.codems.audittrail.domain.task.dto.TaskUpdateRequest;
import com.codems.audittrail.domain.task.mapper.TaskMapper;
import com.codems.audittrail.domain.task.model.Task;
import com.codems.audittrail.domain.task.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.codems.audittrail.domain.audit.annotation.Auditable;
import com.codems.audittrail.domain.audit.model.AuditAction;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public PageableResponse<TaskResponse> findAll(Long ownerId, Pageable pageable) {
        Page<TaskResponse> page = taskRepository.findOwned(ownerId, pageable)
                .map(taskMapper::toResponse);
        return PageableResponse.from(page);
    }

    public TaskResponse findById(Long id, Long ownerId) {
        return taskMapper.toResponse(findOwnedTask(id, ownerId));
    }

    @Transactional
    @Auditable(action=AuditAction.TASK_CREATED, resourceType="TASK", resourceId="#result.id")
    public TaskResponse create(TaskCreateRequest request, Long ownerId) {
        Task task = Task.create(request.title(), request.description(), ownerId);
        return taskMapper.toResponse(taskRepository.save(task));
    }

    @Transactional
    @Auditable(action=AuditAction.TASK_UPDATED, resourceType="TASK", resourceId="#id")
    public TaskResponse update(Long id, TaskUpdateRequest request, Long ownerId) {
        Task task = findOwnedTask(id, ownerId);
        task.update(request.title(), request.description());
        return taskMapper.toResponse(task);
    }

    @Transactional
    @Auditable(action=AuditAction.TASK_DELETED, resourceType="TASK", resourceId="#id")
    public void delete(Long id, Long ownerId) {
        Task task = findOwnedTask(id, ownerId);
        taskRepository.delete(task);
    }

    private Task findOwnedTask(Long id, Long ownerId) {
        return taskRepository.findOwned(id, ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));
    }
}
