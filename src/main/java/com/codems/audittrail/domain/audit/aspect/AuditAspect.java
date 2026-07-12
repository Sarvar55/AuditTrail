package com.codems.audittrail.domain.audit.aspect;

import com.codems.audittrail.common.security.service.CurrentUserService;
import com.codems.audittrail.domain.audit.annotation.Auditable;
import com.codems.audittrail.domain.audit.service.AuditWriter;
import com.codems.audittrail.domain.audit.support.ClientIpResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditWriter auditWriter;
    private final CurrentUserService currentUserService;
    private final ClientIpResolver clientIpResolver;
    private final SpelExpressionParser expressionParser = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer parameterNames = new DefaultParameterNameDiscoverer();


    @Around("@annotation(auditable)")
    public Object audit(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        Object result = joinPoint.proceed();
        try {
            auditWriter.write(currentUserService.getCurrentUserId(), auditable.action(),
                    auditable.resourceType(), resolveResourceId(joinPoint, auditable.resourceId(), result),
                    resolveIpAddress());
        } catch (RuntimeException exception) {
            log.error("Audit event could not be written for action {}", auditable.action(), exception);
        }
        return result;
    }

    private String resolveResourceId(ProceedingJoinPoint joinPoint, String expression, Object result) {
        if (expression.isBlank()) {
            return null;
        }
        var method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        var context = new MethodBasedEvaluationContext(null, method, joinPoint.getArgs(), parameterNames);
        context.setVariable("result", result);
        Object value = expressionParser.parseExpression(expression).getValue(context);
        return value == null ? null : value.toString();
    }

    private String resolveIpAddress() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            HttpServletRequest request = attributes.getRequest();
            return clientIpResolver.resolve(request);
        }
        return null;
    }
}
