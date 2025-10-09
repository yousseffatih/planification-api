package com.WALID.planification_api.services.planification;

import java.util.List;

import com.WALID.planification_api.entities.Planifications;
import com.WALID.planification_api.playload.DTO.PageableResponseDTO;
import com.WALID.planification_api.playload.DTO.PlanificationInfo;
import com.WALID.planification_api.playload.DTO.PlanificationsDTO;

public interface InPlanificationService {
	
	public Planifications addPlanification(PlanificationsDTO planificationsDTO) throws Exception;

	public List<PlanificationsDTO> listplanification(String dateDebut , String dateFin);

	public PlanificationsDTO getPlanification(Long id);

	public void updatePlanification(PlanificationsDTO planificationsDT, Long id) throws Exception;

	public void deletePlanification(Long id);
	
	public PlanificationInfo getInfoPlanification();
	
	public PageableResponseDTO findFilteredPlanification(
	         Long idSalle,
	         Long idModule,
	         Long idType,
	         String du,
	         String au,
	         Long idProf , 
	         Long idCumpus, 
	         Long idVille,
	        int pageNo , int pageSize
	    );

}
