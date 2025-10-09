package com.WALID.planification_api.services.planification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.WALID.planification_api.constants.GlobalConstant;
import com.WALID.planification_api.entities.Classes;
import com.WALID.planification_api.entities.ListAttribut;
import com.WALID.planification_api.entities.Modules;
import com.WALID.planification_api.entities.PlanificationClasse;
import com.WALID.planification_api.entities.Planifications;
import com.WALID.planification_api.entities.Professeur;
import com.WALID.planification_api.entities.Salles;
import com.WALID.planification_api.entities.Users;
import com.WALID.planification_api.playload.ResourceNotFoundException;
import com.WALID.planification_api.playload.DTO.PageableResponseDTO;
import com.WALID.planification_api.playload.DTO.PlanificationInfo;
import com.WALID.planification_api.playload.DTO.PlanificationsDTO;
import com.WALID.planification_api.playload.DTO.PlanificationsDTOProjection;
import com.WALID.planification_api.repositories.UserRepository;
import com.WALID.planification_api.repositories.Parametrage.ClassesRepository;
import com.WALID.planification_api.repositories.Parametrage.ListAttributRepository;
import com.WALID.planification_api.repositories.Parametrage.ModulesRepository;
import com.WALID.planification_api.repositories.Parametrage.ProfesseurRepository;
import com.WALID.planification_api.repositories.Parametrage.SallesRepository;
import com.WALID.planification_api.repositories.planification.PlanificationClasseRepository;
import com.WALID.planification_api.repositories.planification.PlanificationRepository;

@Service
public class PlanificationService implements InPlanificationService{
	@Autowired
	private PlanificationRepository planificationRepository;

	@Autowired
	private SallesRepository sallesRepository;

	@Autowired
	private ClassesRepository classesRepository;

	@Autowired
	private ModulesRepository modulesRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProfesseurRepository professeurRepository;
	
	@Autowired
	private PlanificationClasseRepository planificationClasseRepository;
	
	@Autowired
	private ListAttributRepository listAttributRepository; 

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Planifications addPlanification(PlanificationsDTO planificationsDTO) throws Exception {
	    
	    List<Planifications> planifications = planificationRepository
	        .findByDateRangeOrderByDateAndTime(planificationsDTO.getTimeDebut(), planificationsDTO.getTimeFin());
	    
	    if (!planifications.isEmpty()) {
	        throw new Exception("Planification déjà existante à ce moment");
	    }

	    Salles salles = sallesRepository.findByIdAndStatut(planificationsDTO.getIdSalle(), GlobalConstant.STATUT_ACTIF)
	        .orElseThrow(() -> new ResourceNotFoundException("Salle", "id", planificationsDTO.getIdSalle()));

	    Modules modules = modulesRepository.findByIdStatut(planificationsDTO.getIdModule())
	        .orElseThrow(() -> new ResourceNotFoundException("Module", "id", planificationsDTO.getIdModule()));

	    Users users = userRepository.findByIdAndStatutList(planificationsDTO.getIdUser())
	        .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", planificationsDTO.getIdUser()));

	    Professeur prof = professeurRepository.findByIdStatut(planificationsDTO.getIdProfesseur())
	        .orElseThrow(() -> new ResourceNotFoundException("Professeur", "id", planificationsDTO.getIdProfesseur()));

	    ListAttribut typePlanification = listAttributRepository.findByIdAndStatut(
	        planificationsDTO.getIdTypePlanification(), GlobalConstant.STATUT_ACTIF)
	        .orElseThrow(() -> new ResourceNotFoundException("Type Plaification", "id", planificationsDTO.getIdTypePlanification()));

	    // 👉 First calculate total effectif
	    int totaleElement = 0;
	    List<Classes> classes = new ArrayList<>();
	    for (Long id : planificationsDTO.getIdsClasses()) {
	        Classes cls = classesRepository.findByIdStatut(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Classe", "id", id));
	        totaleElement += cls.getNomberEff();
	        classes.add(cls);
	    }

	    // 👉 Check salle capacity BEFORE saving
	    if (salles.getMaxEffective() < totaleElement) {
	        throw new Exception("Le nombre total de classes est supérieur au nombre de salles effectives.");
	    }

	    // Now create and save the planification
	    Planifications planification = new Planifications();
	    planification.setDateCreation(new Date());
	    planification.setStatut(GlobalConstant.STATUT_ACTIF);
	    planification.setNom(planificationsDTO.getNom());
	    planification.setDatePlanification(planificationsDTO.getDatePlanification());
	    planification.setModule(modules);
	    planification.setTimeDebut(planificationsDTO.getTimeDebut());
	    planification.setTimeFin(planificationsDTO.getTimeFin());
	    planification.setUser(users);
	    planification.setSalle(salles);
	    planification.setProfesseur(prof);
	    planification.setTypePlanification(typePlanification);

	    planification = planificationRepository.save(planification);

	    // Save planification classes
	    for (Classes cls : classes) {
	        PlanificationClasse planificationClasse = new PlanificationClasse();
	        planificationClasse.setClasses(cls);
	        planificationClasse.setPlanifications(planification);
	        planificationClasse.setDateCreation(new Date());
	        planificationClasse.setStatut(GlobalConstant.STATUT_ACTIF);

	        planificationClasseRepository.save(planificationClasse);
	    }

	    return planification;
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updatePlanification(PlanificationsDTO planificationsDTO, Long id) throws Exception{

	    // 1️⃣ Load existing planification
	    Planifications planification = planificationRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Planification", "id", id));

	    // 2️⃣ Load required entities
	    Users users = userRepository.findByIdAndStatutList(planificationsDTO.getIdUser())
	            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", planificationsDTO.getIdUser()));

	    Modules module = modulesRepository.findByIdStatut(planificationsDTO.getIdModule())
	            .orElseThrow(() -> new ResourceNotFoundException("Module", "id", planificationsDTO.getIdModule()));

	    Salles salle = sallesRepository.findById(planificationsDTO.getIdSalle())
	            .orElseThrow(() -> new ResourceNotFoundException("Salle", "id", planificationsDTO.getIdSalle()));

	    ListAttribut typePlanification = listAttributRepository.findByIdAndStatut(
	            planificationsDTO.getIdTypePlanification(), GlobalConstant.STATUT_ACTIF)
	            .orElseThrow(() -> new ResourceNotFoundException("Type Planification", "id", planificationsDTO.getIdTypePlanification()));

	    Professeur prof = professeurRepository.findByIdStatut(planificationsDTO.getIdProfesseur())
	            .orElseThrow(() -> new ResourceNotFoundException("Professeur", "id", planificationsDTO.getIdProfesseur()));

	    // 3️⃣ Validate salle capacity BEFORE saving anything
	    int totalEffectif = 0;
	    List<Classes> classes = new ArrayList<>();
	    for (Long idc : planificationsDTO.getIdsClasses()) {
	        Classes cls = classesRepository.findByIdStatut(idc)
	                .orElseThrow(() -> new ResourceNotFoundException("Classe", "id", idc));
	        totalEffectif += cls.getNomberEff();
	        classes.add(cls);
	    }

	    if (salle.getMaxEffective() < totalEffectif) {
	        throw new Exception(
	                "Le nombre total de classes est supérieur à la capacité de la salle.");
	    }

	    // 4️⃣ Update planification fields
	    planification.setNom(planificationsDTO.getNom());
	    planification.setDatePlanification(planificationsDTO.getDatePlanification());
	    planification.setModule(module);
	    planification.setTimeDebut(planificationsDTO.getTimeDebut());
	    planification.setTimeFin(planificationsDTO.getTimeFin());
	    planification.setUser(users);
	    planification.setSalle(salle);
	    planification.setProfesseur(prof);
	    planification.setTypePlanification(typePlanification);
	    planification.setDateModification(new Date()); // ✅ use dateModification instead of overwriting dateCreation
	    planification.setStatut(GlobalConstant.STATUT_ACTIF);

	    planificationRepository.save(planification);

	    // 5️⃣ Mark old associations as deleted
	    List<PlanificationClasse> existingClasses = planificationClasseRepository.findByPlanificationsId(id);
	    for (PlanificationClasse plc : existingClasses) {
	        plc.setStatut(GlobalConstant.STATUT_DELETE);
	        planificationClasseRepository.save(plc);
	    }

	    // 6️⃣ Add new associations
	    for (Classes cls : classes) {
	        PlanificationClasse newPlc = new PlanificationClasse();
	        newPlc.setClasses(cls);
	        newPlc.setPlanifications(planification);
	        newPlc.setDateCreation(new Date());
	        newPlc.setStatut(GlobalConstant.STATUT_ACTIF);
	        planificationClasseRepository.save(newPlc);
	    }
	}


	@Override
	public List<PlanificationsDTO> listplanification(String dateDebut , String dateFin) {
		List<Planifications> list = planificationRepository.findPlanificationsBetweenDates(dateDebut,dateFin);
		return list.stream().map((e) -> mapToDTO(e)).collect(Collectors.toList());
	}

	@Override
	public PlanificationsDTO getPlanification(Long id) {

		Planifications planifications = planificationRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Planification", "id", id));
		Users users = userRepository.findById(planifications.getUser().getId()).orElseThrow(()-> new ResourceNotFoundException("Utilisateur", "id", planifications.getUser().getId()));
		Modules module = modulesRepository.findById(planifications.getModule().getId()).orElseThrow(()-> new ResourceNotFoundException("Module", "id", planifications.getModule().getId()));
		Salles salle = sallesRepository.findById(planifications.getSalle().getId()).orElseThrow(()-> new ResourceNotFoundException("Salle", "id", planifications.getSalle().getId()));
		ListAttribut listAttribut = listAttributRepository.findById(planifications.getTypePlanification().getId()).orElseThrow(()-> new ResourceNotFoundException("Type de salle", "id", planifications.getTypePlanification().getId()));
		Professeur professeur = professeurRepository.findById(planifications.getProfesseur().getId()).orElseThrow(() -> new ResourceNotFoundException("Professeur", "id", planifications.getProfesseur().getId()));
				
				
	    PlanificationsDTO planificationsDTO = new PlanificationsDTO();
	    planificationsDTO.setId(planifications.getId());
	    planificationsDTO.setDateCreation(planifications.getDateCreation());
	    planificationsDTO.setDateModification(planifications.getDateModification());
	    planificationsDTO.setDatePlanification(planifications.getDatePlanification());
	    planificationsDTO.setDateDesactivation(planifications.getDateDesactivation());
	    planificationsDTO.setStatut(planifications.getStatut());
	    planificationsDTO.setNom(planifications.getNom());
	    planificationsDTO.setDescription(planifications.getDescription());

	    planificationsDTO.setProf(planifications.getProf());
	    planificationsDTO.setTimeDebut(planifications.getTimeDebut());
	    planificationsDTO.setTimeFin(planifications.getTimeFin());

	    planificationsDTO.setIdModule(module.getId());
	    planificationsDTO.setLibelleModule(module.getNom());

	    planificationsDTO.setIdSalle(salle.getId());
	    planificationsDTO.setLibelleSalle(salle.getNom());
	    
	    planificationsDTO.setIdTypePlanification(listAttribut.getId());
	    planificationsDTO.setLibelleTypePlanification(listAttribut.getLibelle());
	    
	    planificationsDTO.setIdProfesseur(professeur.getId());
	    planificationsDTO.setLibelleProfeseur(professeur.getNom()+" "+professeur.getPrenom());

	    planificationsDTO.setIdUser(users.getId());
	    planificationsDTO.setLibelleUser(users.getNom()+" "+users.getPrenom());
	    
	    
	    planificationsDTO.setStatut(planifications.getStatut());

	    List<PlanificationClasse> planificationClasses = planificationClasseRepository.findByPlanificationsIdAndStatut(id , GlobalConstant.STATUT_ACTIF);
	    List<Long> list = new ArrayList<>();
	    for(PlanificationClasse planificationClasse : planificationClasses)
	    {
	    	list.add(planificationClasse.getClasses().getId());
	    }
	    planificationsDTO.setIdsClasses(list);
	    return planificationsDTO;
	}


	@Override
	public void deletePlanification(Long id) {

		Planifications planifications = planificationRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Planification", "id", id));

		planifications.setDateDesactivation(new Date());
		planifications.setStatut(GlobalConstant.STATUT_DELETE);

		planificationRepository.save(planifications);
	}


	private PlanificationsDTO mapToDTO(Planifications x)
	{
		PlanificationsDTO dto = new PlanificationsDTO();

		dto.setId(x.getId());
		dto.setNom(x.getNom());
		dto.setDateCreation(x.getDateCreation());
		dto.setStatut(x.getStatut());
		dto.setDateDesactivation(x.getDateDesactivation());
		dto.setDescription(x.getDescription());
		dto.setDateModification(x.getDateModification());
		dto.setDatePlanification(x.getDatePlanification());
		dto.setProf(x.getProf());
		dto.setTimeDebut(x.getTimeDebut());
		dto.setTimeFin(x.getTimeFin());
		
		dto.setIdModule(x.getModule().getId());
		dto.setLibelleModule(x.getModule().getNom());
		
		dto.setIdSalle(x.getSalle().getId());
		dto.setLibelleSalle(x.getSalle().getNom());
		
		dto.setIdProfesseur(x.getProfesseur().getId());
		dto.setLibelleProfeseur(x.getProfesseur().getNom());
		
		dto.setIdProfesseur(x.getProfesseur().getId());
		dto.setLibelleProfeseur(x.getProfesseur().getNom());
		
		dto.setIdTypePlanification(x.getTypePlanification().getId());
		dto.setLibelleTypePlanification(x.getTypePlanification().getLibelle());
		
		
		dto.setIdUser(x.getUser().getId());

		return dto;
	}
	
	@Override
	public PageableResponseDTO findFilteredPlanification(Long idSalle, Long idModule, Long idType, String du, String au,
			 Long idProf, Long idCumpus, Long idVille , int pageNo , int pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<PlanificationsDTOProjection> v = planificationRepository.findFilteredPlanification(idSalle, idModule, idType, du, au, idProf,idCumpus,idVille, pageable);
		List<PlanificationsDTOProjection> plan = v.getContent();
		PageableResponseDTO  pageableResponseDTO = new PageableResponseDTO();
		pageableResponseDTO.setContent(plan);
		pageableResponseDTO.setPageNo(pageNo);
		pageableResponseDTO.setPageSize(pageSize);
		pageableResponseDTO.setTotalElement(v.getTotalElements());
		pageableResponseDTO.setTotlaPages(v.getTotalPages());
		pageableResponseDTO.setLast(v.isLast());
		return pageableResponseDTO;
	}

	@Override
	public PlanificationInfo getInfoPlanification() {
		  PlanificationInfo info = new PlanificationInfo();
		  info.setTotalReservation(planificationRepository.countPlanificationsInLast30Days(30));
		  info.setSalleDesiponible(sallesRepository.countSallesByStatut(GlobalConstant.STATUT_ACTIF));
		  info.setSalleIndesponible(sallesRepository.countSallesByStatut(GlobalConstant.STATUT_INACTIF));
		  info.setUtilisateurActif(userRepository.countUsersByStatut(GlobalConstant.STATUT_ACTIF));
		  info.setReservationAnnuler(planificationRepository.countPlanificationByStatut(GlobalConstant.STATUT_DELETE));
		  return info;
	}
}
