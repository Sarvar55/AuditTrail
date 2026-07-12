package com.codems.audittrail.common.audit;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MethodLoggingAspect {

    @Pointcut("execution(* com.codems.audittrail..*.*(..)) " +
            "&& !within(com.codems.audittrail.common.audit..*) " +
            "&& !within(com.codems.audittrail.common.security.filter..*) " +
            "&& !bean(*Properties)")
    void applicationMethods() {
    }

    @Around("applicationMethods()")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        log.info("Entering {}", method);

        try {
            Object result = joinPoint.proceed();
            log.info("Exiting {}", method);
            return result;
        } catch (Throwable exception) {
            log.error("Exception in {}: {}", method, exception.getClass().getSimpleName(), exception);
            throw exception;
        }
    }
}
