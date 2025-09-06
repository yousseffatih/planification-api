package com.WALID.planification_api.repositories.Parametrage;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.WALID.planification_api.entities.Salles;


public interface SallesRepository extends JpaRepository<Salles,Long>{

	@Query("select s"
			+ " from Salles s "
			+ " where s.statut in ('actif', 'inActif') "
            + " order by s.dateCreation ")
	List<Salles> findAllWithStatus();
	
	@Query("select s"
			+ " from Salles s "
			+ " where s.statut in ('actif') "
            + " order by s.dateCreation ")
	List<Salles> getSallesListApi();


    Optional<Salles> findByIdAndStatut(Long id , String statut);
    @Override
	Optional<Salles> findById(Long id);
    boolean existsByNom(String nom);
    boolean existsByNomAndStatut(String nom, String statut);
    boolean existsByIdAndStatut(Long id, String statut);
    
    int countSallesByStatut(String statut);


    @Query("select "
			+ " case when count(s)> 0 then true "
			+ " else false end "
			+ " from Salles s "
			+ " where lower(s.nom) like lower(:val) "
			+ " and s.statut in('actif' , 'inActif') "
			+ " and s.id <> :id ")
	boolean existsByNomModif(@Param("val") String val, @Param("id") Long id);

    @Query("SELECT s FROM Salles s WHERE s.id NOT IN (" +
            "SELECT p.salle.id FROM Planifications p " +
            "WHERE p.datePlanification = :date " +
            "AND ((p.timeDebut BETWEEN :timeStart AND :timeEnd) OR (p.timeFin BETWEEN :timeStart AND :timeEnd)))")
     List<Salles> findAvailableSalles(@Param("date") Date date ,  @Param("timeStart") LocalDateTime timeStart, @Param("timeEnd") LocalDateTime timeEnd);

    @Query("SELECT s FROM Salles s WHERE s.id NOT IN (" +
    	       "SELECT p.salle.id FROM Planifications p " +
    	       "WHERE p.datePlanification = :date " +
    	       "AND ((p.timeDebut BETWEEN :timeStart AND :timeEnd) OR (p.timeFin BETWEEN :timeStart AND :timeEnd))" +
    	       "AND p.salle.id <> :reservedSalleId) ")
     List<Salles> findAvailableSallesModif(@Param("date") Date date ,
    		 @Param("timeStart") LocalDateTime timeStart,
    		 @Param("timeEnd") LocalDateTime timeEnd,
    		 @Param("reservedSalleId") Long reservedSalleId);
}
