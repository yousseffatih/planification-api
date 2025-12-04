package com.WALID.planification_api.configuration;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.WALID.planification_api.entities.trace.TraceContext;
import com.WALID.planification_api.services.TraceContextHolder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;


@Component
public class TraceInterceptor implements HandlerInterceptor {
    
    private static final String TRACE_ID_HEADER = "X-Trace-Id";
    private static final String SPAN_ID_HEADER = "X-Span-Id";
    	
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        TraceContext context = new TraceContext();
        
        // Check if trace ID exists in header (for distributed tracing)
        String traceId = request.getHeader(TRACE_ID_HEADER);
        if (traceId != null && !traceId.isEmpty()) {
            context.setTraceId(traceId);
        }
        
        String parentSpanId = request.getHeader(SPAN_ID_HEADER);
        if (parentSpanId != null && !parentSpanId.isEmpty()) {
            context.setParentSpanId(parentSpanId);
        }
        
        // Extract request metadata
        context.setIpAddress(getClientIpAddress(request));
        context.setUserAgent(request.getHeader("User-Agent"));
        context.setSessionId(request.getSession(false) != null ? 
            request.getSession().getId() : null);
        
        // Extract user ID from security context if available
        context.setUserId(SecurityContextHolder.getContext()
             .getAuthentication().getName());
        
        // Store in thread local
        TraceContextHolder.set(context);
        
        // Add to MDC for logging
        MDC.put("traceId", context.getTraceId());
        MDC.put("spanId", context.getSpanId());
        
        // Add trace ID to response header
        response.setHeader(TRACE_ID_HEADER, context.getTraceId());
        response.setHeader(SPAN_ID_HEADER, context.getSpanId());
        
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                                Object handler, Exception ex) {
        // Clean up
        TraceContextHolder.clear();
        MDC.clear();
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String[] headers = {
            "X-Forwarded-For",
            "X-Real-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
        };
        
        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }
        
        return request.getRemoteAddr();
    }
}