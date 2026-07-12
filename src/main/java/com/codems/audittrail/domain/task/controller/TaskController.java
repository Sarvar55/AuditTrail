package com.codems.audittrail.domain.task.controller;

import com.codems.audittrail.common.security.service.CurrentUserService;
import com.codems.audittrail.common.base.PageableResponse;
import com.codems.audittrail.common.base.BaseResponse;
import com.codems.audittrail.domain.task.dto.TaskCreateRequest;
import com.codems.audittrail.domain.task.dto.TaskResponse;
import com.codems.audittrail.domain.task.dto.TaskUpdateRequest;
import com.codems.audittrail.domain.task.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final CurrentUserService currentUserService;

    @GetMapping
    @Operation(summary = "List own tasks", description = "Returns only tasks owned by the authenticated user")
    public ResponseEntity<BaseResponse<PageableResponse<TaskResponse>>> findAll(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(BaseResponse.success(taskService.findAll(currentUserService.getCurrentUserId(), pageable)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get own task", description = "Returns the task only when it belongs to the authenticated user")
    public ResponseEntity<BaseResponse<TaskResponse>> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(BaseResponse.success(taskService.findById(id, currentUserService.getCurrentUserId())));
    }

    @PostMapping
    @Operation(summary = "Create task", description = "Creates a task owned by the authenticated user")
    public ResponseEntity<BaseResponse<TaskResponse>> create(
            @Valid @RequestBody TaskCreateRequest request
    ) {
        TaskResponse response = taskService.create(request, currentUserService.getCurrentUserId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.success(response, HttpStatus.CREATED, "Task created successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update own task", description = "Updates the task only when it belongs to the authenticated user")
    public ResponseEntity<BaseResponse<TaskResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateRequest request
    ) {
        return ResponseEntity.ok(BaseResponse.success(
                taskService.update(id, request, currentUserService.getCurrentUserId()),
                HttpStatus.OK,
                "Task updated successfully"
        ));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete own task", description = "Deletes the task only when it belongs to the authenticated user")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable Long id) {
        taskService.delete(id, currentUserService.getCurrentUserId());
        return ResponseEntity.ok(BaseResponse.success(null, HttpStatus.OK, "Task deleted successfully"));
    }
}
