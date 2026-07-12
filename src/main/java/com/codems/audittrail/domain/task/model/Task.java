package com.codems.audittrail.domain.task.model;

import com.codems.audittrail.common.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tasks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
public class Task extends BaseEntity {

    @Column(name = "title", nullable = false, length = 160)
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    private Task(String title, String description, Long ownerId) {
        this.title = title;
        this.description = description;
        this.ownerId = ownerId;
    }

    public static Task create(String title, String description, Long ownerId) {
        return new Task(title, description, ownerId);
    }

    public void update(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
