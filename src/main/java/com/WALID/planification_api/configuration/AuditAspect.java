package com.WALID.planification_api.configuration;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.domain.Auditable;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.WALID.planification_api.entities.trace.TraceContext;
import com.WALID.planification_api.services.AuditService;
import com.WALID.planification_api.services.TraceContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
@Aspect
public class AuditAspect {
	
private static final Logger logger = LoggerFactory.getLogger(AuditAspect.class);
    
	private final AuditService auditService;
    private final ObjectMapper objectMapper;
    
    public AuditAspect(AuditService auditService, ObjectMapper objectMapper) {
        this.auditService = auditService;
        this.objectMapper = objectMapper;
    }
    
    @Around("@within(org.springframework.web.bind.annotation.RestController) || " +
            "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object auditApiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        ServletRequestAttributes attributes = 
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        
        if (attributes == null) {
            return joinPoint.proceed();
        }
        
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();
        TraceContext context = TraceContextHolder.get();
        
        Object result = null;
        Exception exception = null;
        
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            exception = e;
            throw e;
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            
            try {
                auditService.logApiCall(
                    context,
                    request.getMethod(),
                    request.getRequestURI(),
                    getRequestBody(joinPoint),
                    result,
                    response != null ? response.getStatus() : 500,
                    executionTime,
                    exception
                );
            } catch (Exception e) {
                logger.error("Failed to log audit trail", e);
            }
        }
    }
    
    private String getRequestBody(ProceedingJoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                for (Object arg : args) {
                    if (arg != null && 
                        !isServletObject(arg) && 
                        !isPrimitiveOrWrapper(arg)) {
                        return objectMapper.writeValueAsString(arg);
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to serialize request body", e);
        }
        return null;
    }
    
    private boolean isServletObject(Object obj) {
        return obj instanceof HttpServletRequest || 
               obj instanceof HttpServletResponse;
    }
    
    private boolean isPrimitiveOrWrapper(Object obj) {
        return obj.getClass().isPrimitive() || 
               obj instanceof String ||
               obj instanceof Number ||
               obj instanceof Boolean;
    }
}
