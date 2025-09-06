package com.WALID.planification_api.repositories.planification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.WALID.planification_api.entities.PlanificationClasse;


public interface PlanificationClasseRepository extends JpaRepository<PlanificationClasse,Long> {


	public List<PlanificationClasse> findByPlanificationsId(Long planificationId);

	public List<PlanificationClasse> findByPlanificationsIdAndStatut(Long idLong , String statut);


	public boolean existsByPlanificationsIdAndClassesId(Long planificationId , Long classeId);

}