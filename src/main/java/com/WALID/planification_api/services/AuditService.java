package com.WALID.planification_api.services;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.WALID.planification_api.entities.trace.AuditLogEntity;
import com.WALID.planification_api.entities.trace.TraceContext;
import com.WALID.planification_api.repositories.AuditLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;




import org.springframework.stereotype.Service;

@Service
public class AuditService {
	 private static final Logger logger = LoggerFactory.getLogger(AuditService.class);
	    
	    private final AuditLogRepository auditLogRepository;
	    private final ObjectMapper objectMapper;
	    
	    public AuditService(AuditLogRepository auditLogRepository, ObjectMapper objectMapper) {
	        this.auditLogRepository = auditLogRepository;
	        this.objectMapper = objectMapper;
	    }
	    
	    @Async
	    @Transactional(propagation = Propagation.REQUIRES_NEW)
	    public void logApiCall(TraceContext context, 
	                          String httpMethod,
	                          String endpoint,
	                          String requestBody,
	                          Object response,
	                          int statusCode,
	                          long executionTimeMs,
	                          Exception exception) {
	        try {
	            AuditLogEntity entity = new AuditLogEntity();
	            entity.setTraceId(context.getTraceId());
	            entity.setSpanId(context.getSpanId());
	            entity.setParentSpanId(context.getParentSpanId());
	            entity.setUserId(context.getUserId());
	            entity.setSessionId(context.getSessionId());
	            entity.setHttpMethod(httpMethod);
	            entity.setEndpoint(endpoint);
	            entity.setRequestBody(truncate(requestBody, 10000));
	            entity.setResponseBody(truncate(serializeResponse(response), 10000));
	            entity.setStatusCode(statusCode);
	            entity.setExecutionTimeMs(executionTimeMs);
	            entity.setIpAddress(context.getIpAddress());
	            entity.setUserAgent(context.getUserAgent());
	            entity.setTimestamp(LocalDateTime.now());
	            
	            if (exception != null) {
	                entity.setErrorMessage(exception.getMessage());
	            }
	            
	            auditLogRepository.save(entity);
	            
	            logger.info("API call logged - TraceId: {}, Endpoint: {}, Status: {}, Duration: {}ms",
	                context.getTraceId(), endpoint, statusCode, executionTimeMs);
	        } catch (Exception e) {
	            logger.error("Failed to save audit log", e);
	        }
	    }
	    
	    private String serializeResponse(Object response) {
	        if (response == null) {
	            return null;
	        }
	        try {
	            return objectMapper.writeValueAsString(response);
	        } catch (Exception e) {
	            logger.warn("Failed to serialize response", e);
	            return response.toString();
	        }
	    }
	    
	    private String truncate(String value, int maxLength) {
	        if (value == null) {
	            return null;
	        }
	        return value.length() > maxLength ? 
	            value.substring(0, maxLength) + "..." : value;
	    }
}
