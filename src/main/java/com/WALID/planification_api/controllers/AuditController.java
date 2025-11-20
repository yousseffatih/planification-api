package com.WALID.planification_api.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.WALID.planification_api.entities.trace.AuditLogEntity;
import com.WALID.planification_api.repositories.AuditLogRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for querying audit logs
 */
@RestController
@RequestMapping("/api/audit")
public class AuditController {
    
    private final AuditLogRepository auditLogRepository;
    
    public AuditController(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }
    
    @GetMapping("/trace/{traceId}")
    public ResponseEntity<List<AuditLogEntity>> getByTraceId(@PathVariable String traceId) {
        List<AuditLogEntity> logs = auditLogRepository.findByTraceId(traceId);
        return ResponseEntity.ok(logs);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<AuditLogEntity>> getByUserId(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        Page<AuditLogEntity> logs = auditLogRepository.findByUserId(userId, pageable);
        return ResponseEntity.ok(logs);
    }
    
    @GetMapping("/range")
    public ResponseEntity<Page<AuditLogEntity>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        Page<AuditLogEntity> logs = auditLogRepository.findByDateRange(startDate, endDate, pageable);
        return ResponseEntity.ok(logs);
    }
    
    @GetMapping("/errors")
    public ResponseEntity<Page<AuditLogEntity>> getErrors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        Page<AuditLogEntity> logs = auditLogRepository.findErrorLogs(pageable);
        return ResponseEntity.ok(logs);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<AuditLogEntity>> searchByEndpoint(
            @RequestParam String endpoint,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        Page<AuditLogEntity> logs = auditLogRepository.findByEndpointAndDateRange(
            endpoint, startDate, endDate, pageable);
        return ResponseEntity.ok(logs);
    }
}
