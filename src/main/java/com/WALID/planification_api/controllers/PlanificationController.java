package com.WALID.planification_api.controllers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.WALID.planification_api.constants.GlobalConstant;
import com.WALID.planification_api.playload.MessageResponse;
import com.WALID.planification_api.playload.DTO.GetProfesseurBody;
import com.WALID.planification_api.playload.DTO.PageableResponseDTO;
import com.WALID.planification_api.playload.DTO.PlanificationInfo;
import com.WALID.planification_api.playload.DTO.PlanificationsDTO;
import com.WALID.planification_api.repositories.planification.PlanificationRepository;
import com.WALID.planification_api.services.Security.JWTService;
import com.WALID.planification_api.services.planification.InPlanificationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/planification")
@RequiredArgsConstructor
public class PlanificationController {

	@Autowired
	private InPlanificationService planificationsServices;
	
	@Autowired
	private PlanificationRepository planificationRepository;
	
	@Autowired
	private JWTService jwtService;

	@PostMapping("")
	public ResponseEntity<?> addPlanification(@Valid @RequestBody PlanificationsDTO planificationsDTO)
	{   
		LocalDate today = LocalDate.now();
		LocalDate dateplanification = planificationsDTO.getDatePlanification().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		boolean ifDateStartAfterEnd = planificationsDTO.getTimeDebut().isAfter(planificationsDTO.getTimeFin());
		
		boolean isPlanExite = planificationRepository.existsBySalleAndTimeOverlap(planificationsDTO.getIdSalle(),planificationsDTO.getTimeDebut(),planificationsDTO.getTimeFin());
		
		if(isPlanExite)
		{
			return ResponseEntity.status(GlobalConstant.HTTPSTATUT_RESPONSE_ERORR).body(new MessageResponse("Il existe déjà une planification dans cette salle pour l'intervalle de temps choisi !", "warning") );
		}
		
		if(ifDateStartAfterEnd)
		{
			return ResponseEntity.status(GlobalConstant.HTTPSTATUT_RESPONSE_ERORR).body(new MessageResponse("L'heure de début ne peut pas être supérieure à l'heure de fin !", "warning") );
		}
		if(dateplanification.isBefore(today))
		{
			return ResponseEntity.status(GlobalConstant.HTTPSTATUT_RESPONSE_ERORR).body(new MessageResponse("La date de planification doit être postérieure à la date d'aujourd'hui !", "warning") );
		}
		try {
			planificationsServices.addPlanification(planificationsDTO);
			return ResponseEntity.status(200).body(new MessageResponse("Réservation validée!", "success") );
		} catch (Exception e) {
			return ResponseEntity.status(450).body(new MessageResponse(e.getMessage(), "warning") );
		}

	}

	@GetMapping("")
	public ResponseEntity<List<PlanificationsDTO>> getAllPlanification(
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") String dateDebut,@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") String dateFin)
	{
		List<PlanificationsDTO> list = planificationsServices.listplanification(dateDebut,dateFin);
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
	
	@PostMapping("/filtred")
	public ResponseEntity<PageableResponseDTO> getAllPlanificationFiltred(
			@RequestBody GetProfesseurBody getProfesseurBody,
					@RequestParam(value = "pageNo" , defaultValue = "0", required = false) int pageNo,
					@RequestParam(value = "pageSize" , defaultValue = "5" , required = false) int pageSize)
			
	{
		PageableResponseDTO list = planificationsServices.findFilteredPlanification(getProfesseurBody.getIdSalle() ,getProfesseurBody.getIdModule(),getProfesseurBody.getIdType(),getProfesseurBody.getDateDebut(),getProfesseurBody.getDateFin() , getProfesseurBody.getIdProf() , pageNo, pageSize);
		return new ResponseEntity<>(list,HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PlanificationsDTO> getPlanification(@PathVariable Long id)
	{
		PlanificationsDTO list = planificationsServices.getPlanification(id);
		return new ResponseEntity<>(list,HttpStatus.OK);
	}

	@GetMapping("/delete/{id}")
	public ResponseEntity<?> deletePlanification(@PathVariable Long id)
	{
		planificationsServices.deletePlanification(id);
		return  ResponseEntity.status(200).body(new MessageResponse("Réservation supprimée!", "success") );
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updatePlanification(@Valid @RequestBody PlanificationsDTO planificationsDTO, @PathVariable Long id)
	{
		LocalDate today = LocalDate.now();
		LocalDate dateplanification = planificationsDTO.getDatePlanification().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		boolean isPlanExite = planificationRepository.existsBySalleAndTimeOverlapExcludingId(planificationsDTO.getIdSalle(),planificationsDTO.getTimeDebut(),planificationsDTO.getTimeFin(), id);
		
		if(isPlanExite)
		{
			return ResponseEntity.status(GlobalConstant.HTTPSTATUT_RESPONSE_ERORR).body(new MessageResponse("Il existe déjà une planification dans cette salle pour l'intervalle de temps choisi !", "warning") );
		}

		boolean ifDateStartAfterEnd = planificationsDTO.getTimeDebut().isAfter(planificationsDTO.getTimeFin());
		if(ifDateStartAfterEnd)
		{
			return ResponseEntity.status(GlobalConstant.HTTPSTATUT_RESPONSE_ERORR).body(new MessageResponse("L'heure de début ne peut pas être supérieure à l'heure de fin !", "warning") );
		}
		if(dateplanification.isBefore(today))
		{
			return ResponseEntity.status(GlobalConstant.HTTPSTATUT_RESPONSE_ERORR).body(new MessageResponse("La date de planification doit être postérieure à la date d'aujourd'hui !", "warning") );
		}
		try {
			planificationsServices.updatePlanification(planificationsDTO, id);
			return ResponseEntity.status(200).body(new MessageResponse("Réservation modifiée!", "success") );
		} catch (Exception e) {
			return ResponseEntity.status(450).body(new MessageResponse(e.getMessage(), "warning") );
		}

	}
	
	@GetMapping("/tableauBordInfo")
	public ResponseEntity<PlanificationInfo> getTableauBordInfo()
	{
		PlanificationInfo planificationInfo = planificationsServices.getInfoPlanification();
		return new ResponseEntity<PlanificationInfo>(planificationInfo , HttpStatus.OK);
	}
}