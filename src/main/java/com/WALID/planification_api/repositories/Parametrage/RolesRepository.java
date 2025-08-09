package com.WALID.planification_api.repositories.Parametrage;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.WALID.planification_api.entities.Roles;


public interface RolesRepository extends JpaRepository<Roles, Long>{

	 @Query("select r"
				+ " from Roles r "
				+ " where r.statut in ('actif') "
	            + " order by r.dateCreation ")
		List<Roles> findAllWithStatus();

	    @Query("select r"
				+ " from Roles r "
				+ " where r.statut in ('actif')"
				+ " and r.id = :val ")
		Optional<Roles> findByIdStatut(@Param("val") Long val);

	    boolean existsByNomAndStatut(String nom, String statut);
	    boolean existsByIdAndStatut(Long id, String statut);

	    @Query("select "
				+ " case when count(r)> 0 then true "
				+ " else false end "
				+ " from Roles r "
				+ " where lower(r.nom) like lower(:val) "
				+ " and r.statut in('actif') "
				+ " and r.id <> :id ")
		boolean existsByNomModif(@Param("val") String val, @Param("id") Long id);
}
