package com.WALID.planification_api.repositories.Parametrage;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.WALID.planification_api.entities.ListAttribut;


public interface ListAttributRepository extends JpaRepository<ListAttribut,Long> {

	boolean existsByLibelleAndStatut(String libelle , String statut);

	List<ListAttribut> findAllByListNameApiAndStatut(String nameApi, String statut);

	Optional<ListAttribut> findByIdAndStatut(Long id ,String statut);
}
