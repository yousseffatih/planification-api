package com.WALID.planification_api.repositories.Parametrage;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.WALID.planification_api.entities.Professeur;


public interface ProfesseurRepository extends JpaRepository<Professeur , Long>{
	
    @Query("select c"
			+ " from Professeur c "
			+ " where c.statut in ('actif') "
            + " order by c.dateCreation ")
	List<Professeur> findAllWithStatus();

    @Query("select c"
			+ " from Professeur c "
			+ " where c.statut in ('actif')"
			+ " and c.id = :val ")
	Optional<Professeur> findByIdStatut(@Param("val") Long val);

    boolean existsByNomAndStatut(String nom, String statut);
    boolean existsByIdAndStatut(Long id, String statut);

    @Query("select "
			+ " case when count(c)> 0 then true "
			+ " else false end "
			+ " from Professeur c "
			+ " where lower(c.nom) like lower(:val) "
			+ " and c.statut in('actif') "
			+ " and c.id <> :id ")
	boolean existsByNomModif(@Param("val") String val, @Param("id") Long id);
}
