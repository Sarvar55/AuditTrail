package com.codems.audittrail.common.audit;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MethodLoggingAspect {

    @Around("execution(public * com.codems.audittrail.domain..*(..)) " +
            "|| execution(public * com.codems.audittrail.common.security.service..*(..))")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        log.debug("Entering {}", method);

        try {
            Object result = joinPoint.proceed();
            log.debug("Exiting {}", method);
            return result;
        } catch (Throwable exception) {
            log.error("Exception in {}: {}", method, exception.getClass().getSimpleName(), exception);
            throw exception;
        }
    }
}
