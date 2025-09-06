package com.WALID.planification_api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.WALID.planification_api.entities.Users;


public interface UserRepository extends JpaRepository<Users ,Long>{

	@Query("SELECT us FROM Users us WHERE us.statut IN ('actif') AND us.username =:val ")
	Optional<Users> findByUsernameStatut(@Param("val")String username);

	@Query("SELECT us FROM Users us WHERE us.statut IN ('actif', 'inActif')")
	List<Users> findUsersByStatut();
	
	int countUsersByStatut(String statut);


	@Query("SELECT us FROM Users us WHERE us.statut IN ('actif', 'inActif') AND us.id =:val ")
	Optional<Users> findByIdAndStatutList(@Param("val")Long id);

	@Query("select "
			+ " case when count(u)> 0 then true "
			+ " else false end "
			+ " from Users u "
			+ " where lower(u.username) = lower(:val)  and u.statut in('actif','inActif') ")
	boolean existsByUsernameAndStatut(@Param("val") String val);


}
