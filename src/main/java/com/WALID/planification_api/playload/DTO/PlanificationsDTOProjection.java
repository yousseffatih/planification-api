package com.WALID.planification_api.playload.DTO;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface PlanificationsDTOProjection {
	
    Long getId();
    
    String getNom();
    
    String getStatut();
    
    @JsonFormat(pattern = "dd/MM/yyyy")
    Date getDatePlanification();
    
    //@JsonFormat(pattern = "HH:mm")
    String getTimeDebut();
    
    //@JsonFormat(pattern = "HH:mm")
    String getTimeFin();
    
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
    
    Long getIdTypePlanification();
    
    Long getIdCumpus();
    
    String getLibelleCumpus();
    
    Long getIdVille();
    
    String getLibelleVille();
    
    String getLibelleClasses();
}