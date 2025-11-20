package com.WALID.planification_api.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.WALID.planification_api.entities.trace.AuditLogEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLogEntity, Long> {
    
    List<AuditLogEntity> findByTraceId(String traceId);
    
    Page<AuditLogEntity> findByUserId(String userId, Pageable pageable);
    
    @Query("SELECT a FROM AuditLogEntity a WHERE a.timestamp BETWEEN :startDate AND :endDate")
    Page<AuditLogEntity> findByDateRange(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable
    );
    
    @Query("SELECT a FROM AuditLogEntity a WHERE a.endpoint LIKE %:endpoint% " +
           "AND a.timestamp BETWEEN :startDate AND :endDate")
    Page<AuditLogEntity> findByEndpointAndDateRange(
        @Param("endpoint") String endpoint,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable
    );
    
    @Query("SELECT a FROM AuditLogEntity a WHERE a.statusCode >= 400")
    Page<AuditLogEntity> findErrorLogs(Pageable pageable);
}
