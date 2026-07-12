package com.codems.audittrail.domain.task.repository;

import java.util.Optional;

import com.codems.audittrail.domain.task.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends org.springframework.data.repository.Repository<Task, Long> {

    Task save(Task task);

    void delete(Task task);

    @Query("select task from Task task where task.ownerId = :ownerId")
    Page<Task> findOwned(@Param("ownerId") Long ownerId, Pageable pageable);

    @Query("select task from Task task where task.id = :id and task.ownerId = :ownerId")
    Optional<Task> findOwned(@Param("id") Long id, @Param("ownerId") Long ownerId);
}
