package com.WALID.planification_api.repositories.planification;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.WALID.planification_api.entities.Planifications;
import com.WALID.planification_api.playload.DTO.PlanificationsDTOProjection;



public interface PlanificationRepository extends JpaRepository<Planifications,Long>{

	@Query("SELECT p FROM Planifications p WHERE p.statut IN ('actif') AND p.datePlanification BETWEEN FUNCTION('STR_TO_DATE', :dateDebut, '%d/%m/%Y %H:%i') AND FUNCTION('STR_TO_DATE', :dateFin, '%d/%m/%Y %H:%i')")
	    List<Planifications> findPlanificationsBetweenDates(
	            @Param("dateDebut") String dateDebut,
	            @Param("dateFin") String dateFin);

	public Optional<Planifications> findByIdAndStatut(Long idLong , String statut);
	
	@Query(value = "SELECT COUNT(*) FROM planifications WHERE DATE_PLANIFICATION >= DATE_SUB(CURDATE(), INTERVAL :days DAY)", nativeQuery = true)
    int countPlanificationsInLast30Days(@Param("days") int days);
	
	int countPlanificationByStatut(String statut);
	
	
	@Query(
	        value = """
	            SELECT 
	                p.id AS id,
	                p.nom AS nom,
	                p.date_planification AS datePlanification,
	                p.time_debut AS timeDebut,
	        		DATE_FORMAT(p.time_debut, '%H:%i') AS timeDebut,
	        		DATE_FORMAT(p.time_fin, '%H:%i') AS timeFin,
	                p.statut AS statut,
	                p.prof AS prof,
	                pr.id AS idProfesseur,
	                CONCAT(pr.nom, ' ', pr.prenom) AS libelleProfeseur,
	                m.id AS idModule,
	                m.nom AS libelleModule,
	                s.id AS idSalle,
	                s.nom AS libelleSalle,
	                u.id AS idUser,
	                u.nom AS libelleUser,
	                p.description AS description,
	                tp.libelle AS liblleTypePlanification,
	                tp.id AS idTypePlanification
	            FROM planifications p
	            LEFT JOIN professeur pr ON p.professeur_id = pr.id
	            LEFT JOIN modules m ON p.module_id = m.id
	            LEFT JOIN salles s ON p.salle_id = s.id
	            LEFT JOIN users u ON p.user_id = u.id
	            LEFT JOIN list_attribut tp ON p.type_planification_id = tp.id
	            WHERE (:salle IS NULL OR s.id = :salle)
	              AND (:module IS NULL OR m.id = :module)
	              AND (:typePlanification IS NULL OR tp.id = :typePlanification)
	              AND (:professeur IS NULL OR pr.id = :professeur)
	              AND (:dateDebut IS NULL OR p.date_planification >= STR_TO_DATE(:dateDebut, '%d/%m/%Y'))
	              AND (:dateFin IS NULL OR p.date_planification <= STR_TO_DATE(:dateFin, '%d/%m/%Y'))
	        

	        """,
	        countQuery = """
	            SELECT COUNT(*)
	            FROM planifications p
	            LEFT JOIN professeur pr ON p.professeur_id = pr.id
	            LEFT JOIN modules m ON p.module_id = m.id
	            LEFT JOIN salles s ON p.salle_id = s.id
	            LEFT JOIN users u ON p.user_id = u.id
	            LEFT JOIN list_attribut tp ON p.type_planification_id = tp.id
	            WHERE (:salle IS NULL OR s.id = :salle)
	              AND (:module IS NULL OR m.id = :module)
	              AND (:typePlanification IS NULL OR tp.id = :typePlanification)
	              AND (:professeur IS NULL OR pr.id = :professeur)
	              AND (:dateDebut IS NULL OR p.date_planification >= STR_TO_DATE(:dateDebut, '%d/%m/%Y'))
	              AND (:dateFin IS NULL OR p.date_planification <= STR_TO_DATE(:dateFin, '%d/%m/%Y'))
	        """,
	        nativeQuery = true
	    )
	    Page<PlanificationsDTOProjection> findFilteredPlanification(
	        @Param("salle") Long idSalle,
	        @Param("module") Long idModule,
	        @Param("typePlanification") Long idType,
	        @Param("dateDebut") String du,
	        @Param("dateFin") String au,
	        @Param("professeur") Long idProf,
	        Pageable pageable
	    );
}

