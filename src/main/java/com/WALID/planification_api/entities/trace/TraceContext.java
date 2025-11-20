package com.WALID.planification_api.entities.trace;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TraceContext {
	
	private String traceId;
    private String spanId;
    private String parentSpanId;
    private String userId;
    private String sessionId;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime timestamp;
    
    public TraceContext() {
        this.traceId = UUID.randomUUID().toString();
        this.spanId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
    }
}
