package com.WALID.planification_api.repositories.Parametrage;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.WALID.planification_api.entities.Cumpus;

public interface CumpusRepository extends JpaRepository<Cumpus, Long>{
	
	@Query("select c"
			+ " from Cumpus c "
			+ " where c.statut in ('actif', 'inActif') "
            + " order by c.dateCreation ")
	List<Cumpus> findAllWithStatus();
    
    @Query("select c"
			+ " from Cumpus c "
			+ " where c.statut in ('actif') "
            + " order by c.dateCreation ")
	List<Cumpus> getClassesListApi();


    @Query("select c"
			+ " from Cumpus c "
			+ " where c.statut in ('actif',  'inActif')"
			+ " and c.id = :val ")
	Optional<Cumpus> findByIdStatut(@Param("val") Long val);

    boolean existsByNomAndStatut(String nom, String statut);
    boolean existsByIdAndStatut(Long id, String statut);

    @Query("select "
			+ " case when count(c)> 0 then true "
			+ " else false end "
			+ " from Cumpus c "
			+ " where lower(c.nom) like lower(:val) "
			+ " and c.statut in('actif' , 'inActif') "
			+ " and c.id <> :id ")
	boolean existsByNomModif(@Param("val") String val, @Param("id") Long id);
}
