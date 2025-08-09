package com.WALID.planification_api.repositories.Parametrage;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.WALID.planification_api.entities.Modules;


public interface ModulesRepository extends JpaRepository<Modules,Long>{

	   @Query("select m"
				+ " from Modules m "
				+ " where m.statut in ('actif') "
	            + " order by m.dateCreation ")
		List<Modules> findAllWithStatus();

	    @Query("select m"
				+ " from Modules m "
				+ " where m.statut in ('actif')"
				+ " and m.id = :val ")
		Optional<Modules> findByIdStatut(@Param("val") Long val);

	    boolean existsByNomAndStatut(String nom, String statut);
	    boolean existsByIdAndStatut(Long id, String statut);

	    @Query("select "
				+ " case when count(m)> 0 then true "
				+ " else false end "
				+ " from Modules m "
				+ " where lower(m.nom) like lower(:val) "
				+ " and m.statut in('actif') "
				+ " and m.id <> :id ")
		boolean existsByNomModif(@Param("val") String val, @Param("id") Long id);
}
