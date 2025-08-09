package com.WALID.planification_api.playload.DTO;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface PlanificationsDTOProjection {
	
    Long getId();
    
    String getNom();
    
    String getStatut();
    
    @JsonFormat(pattern = "dd/MM/yyyy")
    Date getDatePlanification();
    
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDateTime getTimeDebut();
    
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDateTime getTimeFin();
    
    String getProf();
    
    Long getIdProfesseur();
    
    String getLibelleProfeseur();
    
    Long getIdModule();
    
    String getLibelleModule();
    
    Long getIdSalle();
    
    String getLibelleSalle();
    
    Long getIdUser();
    
    String getLibelleUser();
    
    String getDescription();
    
    String getLiblleTypePlanification();
    
    String getIdTypePlanification();
}