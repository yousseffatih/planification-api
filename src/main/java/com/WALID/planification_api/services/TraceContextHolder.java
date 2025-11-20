package com.WALID.planification_api.services;

import com.WALID.planification_api.entities.trace.TraceContext;

public class TraceContextHolder {
    
    private static final ThreadLocal<TraceContext> contextHolder = new ThreadLocal<>();
    
    public static void set(TraceContext context) {
        contextHolder.set(context);
    }
    
    public static TraceContext get() {
        TraceContext context = contextHolder.get();
        if (context == null) {
            context = new TraceContext();
            contextHolder.set(context);
        }
        return context;
    }
    
    public static void clear() {
        contextHolder.remove();
    }
}
