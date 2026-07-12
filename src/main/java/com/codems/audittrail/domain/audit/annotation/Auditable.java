package com.codems.audittrail.domain.audit.annotation;

import com.codems.audittrail.domain.audit.model.AuditAction;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auditable {
    AuditAction action();

    String resourceType();

    String resourceId() default "";

}
