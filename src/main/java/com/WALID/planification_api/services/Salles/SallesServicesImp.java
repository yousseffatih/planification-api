package com.WALID.planification_api.services.Salles;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.WALID.planification_api.constants.GlobalConstant;
import com.WALID.planification_api.entities.ListAttribut;
import com.WALID.planification_api.entities.Salles;
import com.WALID.planification_api.playload.ResourceNotFoundException;
import com.WALID.planification_api.playload.DTO.ListAttributAUTO;
import com.WALID.planification_api.playload.DTO.SallesDTO;
import com.WALID.planification_api.repositories.Parametrage.ListAttributRepository;
import com.WALID.planification_api.repositories.Parametrage.SallesRepository;



@Service
public class SallesServicesImp implements InSallesServices{

	@Autowired
	private SallesRepository sallesRepository;

	@Autowired
	private ListAttributRepository listAttributRepository;

	@Override
	public List<SallesDTO> getAllSalles() {
		List<Salles> salles = sallesRepository.findAllWithStatus();
        return  salles.stream().map((c) -> mapToDTO(c)).collect(Collectors.toList());
	}

	@Override
	public List<SallesDTO> availableSalles(Date date , LocalDateTime timeStar , LocalDateTime timeEnd) {
		List<Salles> salles = sallesRepository.findAvailableSalles(date , timeStar , timeEnd);
	    return  salles.stream().map((c) -> mapToDTO(c)).collect(Collectors.toList());
	}

	@Override
	public List<SallesDTO> availableSallesModif(Date date , LocalDateTime timeStar , LocalDateTime timeEnd, Long id) {
		List<Salles> salles = sallesRepository.findAvailableSallesModif(date , timeStar , timeEnd,id);
	    return  salles.stream().map((c) -> mapToDTO(c)).collect(Collectors.toList());
	}

	@Override
	public List<ListAttributAUTO> gettAllSallesApi() {
		List<Salles> salles = sallesRepository.findAllWithStatus();
        return  salles.stream().map((c) -> mapToList(c)).collect(Collectors.toList());
	}

	@Override
	public SallesDTO getSalleById(Long id) {
		Salles salle = sallesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Salle","id",id));
        return  mapToDTO(salle);
	}

	@Override
	public SallesDTO addSalle(SallesDTO sallesDTO) {
		Salles salles = new Salles();

		ListAttribut typeSalle = listAttributRepository.findByIdAndStatut(sallesDTO.getIdTypeSalle() , GlobalConstant.STATUT_ACTIF)
				.orElseThrow(() -> new ResourceNotFoundException("Type Salle","id",sallesDTO.getIdTypeSalle()));

		salles.setDateCreation(new Date());
		salles.setStatut(sallesDTO.getStatut());
		salles.setNom(sallesDTO.getNom());
		salles.setMaxEffective(sallesDTO.getMaxEffective());
		salles.setTypeSalle(typeSalle);
		Salles rl = sallesRepository.save(salles);
	    return mapToDTO(rl);
	}

	@Override
	public void deleteSalle(Long id) {
		Salles role = sallesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Salle", "id", id));
		role.setDateDesactivation(new Date());
		role.setStatut(GlobalConstant.STATUT_DELETE);
		sallesRepository.save(role);

	}

	@Override
	public SallesDTO updateSalle(Long id, SallesDTO sallesDTO) {
		Salles salles = sallesRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Salle", "id", id));
		ListAttribut typeSalleAttribut = listAttributRepository.findByIdAndStatut(sallesDTO.getIdTypeSalle() , GlobalConstant.STATUT_ACTIF).orElseThrow(()-> new ResourceNotFoundException("Type de salle", "id", sallesDTO.getIdTypeSalle()));
		salles.setNom(sallesDTO.getNom());
		salles.setStatut(sallesDTO.getStatut());
		salles.setDateModification(new Date());
		salles.setMaxEffective(sallesDTO.getMaxEffective());
		salles.setTypeSalle(typeSalleAttribut);		
		Salles sl = sallesRepository.save(salles);
		return mapToDTO(sl);
	}

	private SallesDTO mapToDTO(Salles x)
    {
		SallesDTO dto = new SallesDTO();
        dto.setId(x.getId());
        dto.setNom(x.getNom());
        dto.setMaxEffective(x.getMaxEffective());

        dto.setIdTypeSalle(x.getTypeSalle().getId());
        dto.setLibelleTypeSalle(x.getTypeSalle().getLibelle());
        dto.setStatut(x.getStatut());
        dto.setDateCreation(x.getDateCreation());
        dto.setDateDesactivation(x.getDateDesactivation());
        dto.setDateModification(x.getDateModification());
        return dto;
    }

	private ListAttributAUTO mapToList(Salles x)
    {
		ListAttributAUTO dto = new ListAttributAUTO();
        dto.setId(x.getId());
        dto.setLibelle(x.getNom());

        return dto;
    }



}
