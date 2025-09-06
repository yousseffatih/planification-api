package com.WALID.planification_api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.WALID.planification_api.entities.UserRole;


public interface UserRoleRepository extends JpaRepository<UserRole,Long>{

	@Query("SELECT d "
	 		+ " FROM UserRole d "
	 		+ " WHERE user.id = :idUser "
	 		+ " and d.statut = 'actif' ")
	 List<UserRole> getDroits(@Param("idUser") Long idUser);
}
