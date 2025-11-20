package com.WALID.planification_api.entities.trace;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "audit_logs", indexes = {
	    @Index(name = "trace_id", columnList = "traceId"),
	    @Index(name = "user_id", columnList = "userId"),
	})
public class AuditLogEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String traceId;
    
    @Column(length = 100)
    private String spanId;
    
    @Column(length = 100)
    private String parentSpanId;
    
    @Column(length = 100)
    private String userId;
    
    @Column(length = 100)
    private String sessionId;
    
    @Column(nullable = false)
    private String httpMethod;
    
    @Column(nullable = false, length = 1000)
    private String endpoint;
    
    @Column(columnDefinition = "TEXT")
    private String requestBody;
    
    @Column(columnDefinition = "TEXT")
    private String responseBody;
    
    @Column(nullable = false)
    private Integer statusCode;
    
    @Column(nullable = false)
    private Long executionTimeMs;
    
    @Column(length = 50)
    private String ipAddress;
    
    @Column(length = 500)
    private String userAgent;
    
    @Column(columnDefinition = "TEXT")
    private String errorMessage;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
}
