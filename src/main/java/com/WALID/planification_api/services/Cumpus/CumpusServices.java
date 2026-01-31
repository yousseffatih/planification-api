package com.WALID.planification_api.services.Cumpus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.WALID.planification_api.constants.GlobalConstant;
import com.WALID.planification_api.entities.Cumpus;
import com.WALID.planification_api.entities.Villes;
import com.WALID.planification_api.playload.ResourceNotFoundException;
import com.WALID.planification_api.playload.DTO.CumpusDTO;
import com.WALID.planification_api.playload.DTO.ListAttributAUTO;
import com.WALID.planification_api.repositories.Parametrage.CumpusRepository;
import com.WALID.planification_api.repositories.Parametrage.VillesRepository;

@Service
public class CumpusServices implements InCumpusService {

	@Autowired
	private CumpusRepository cumpusRepository;

	@Autowired
	private VillesRepository villesRepository;

	@Override
	public List<CumpusDTO> getAllCumpus() {

		List<Cumpus> cumpus = new ArrayList<>(cumpusRepository.findAllWithStatus());

		Cumpus addCumpus = new Cumpus();
		addCumpus.setId(0L);
		addCumpus.setNom("TOUS LES CUMPUS");

		cumpus.add(addCumpus);

		return cumpus.stream().map((c) -> mapToDTO(c)).collect(Collectors.toList());
	}

	@Override
	public List<ListAttributAUTO> getCumpusListApi() {
		List<Cumpus> cumpus = new ArrayList<>(cumpusRepository.getClassesListApi());

		Cumpus addCumpus = new Cumpus();
		addCumpus.setId(0L);
		addCumpus.setNom("TOUS LES CUMPUS");
		return cumpus.stream().map((c) -> mapToList(c)).collect(Collectors.toList());
	}

	@Override
	public CumpusDTO getCumpusById(Long id) {
		Cumpus cumpus = cumpusRepository.findByIdStatut(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cumpus", "id", id));
		return mapToDTO(cumpus);
	}

	@Override
	public CumpusDTO addCumpus(CumpusDTO cumpusDTO) {
		Cumpus cumpus = new Cumpus();

		Villes ville = villesRepository.findByIdStatut(cumpusDTO.getIdVille())
				.orElseThrow(() -> new ResourceNotFoundException("Ville", "id", cumpusDTO.getIdVille()));

		cumpus.setDateCreation(new Date());
		cumpus.setStatut(GlobalConstant.STATUT_ACTIF);
		cumpus.setNom(cumpusDTO.getNom());
		cumpus.setVille(ville);
		cumpusRepository.save(cumpus);
		return mapToDTO(cumpus);
	}

	@Override
	public void deleteCumpus(Long id, String motif) {
		Cumpus cumpus = cumpusRepository.findByIdStatut(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cumpus", "id", id));
		cumpus.setStatut(GlobalConstant.STATUT_DELETE);
		cumpus.setDateDesactivation(new Date());
		cumpus.setMotif(motif);
		cumpusRepository.save(cumpus);

	}

	@Override
	public CumpusDTO updateCumpus(Long id, CumpusDTO cumpusDTO) {
		Cumpus cumpus = cumpusRepository.findByIdStatut(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cumpus", "id", id));
		Villes ville = villesRepository.findByIdStatut(cumpusDTO.getIdVille())
				.orElseThrow(() -> new ResourceNotFoundException("Ville", "id", cumpusDTO.getIdVille()));

		cumpus.setDateCreation(new Date());
		cumpus.setStatut(cumpusDTO.getStatut());
		cumpus.setNom(cumpusDTO.getNom());
		cumpus.setVille(ville);
		cumpusRepository.save(cumpus);
		return mapToDTO(cumpus);
	}

	private CumpusDTO mapToDTO(Cumpus x) {
		CumpusDTO dto = new CumpusDTO();
		dto.setId(x.getId());
		dto.setNom(x.getNom());
		Optional.ofNullable(x.getVille())
				.ifPresent(ville -> {
					dto.setIdVille(ville.getId());
					dto.setLibelleVille(ville.getNom());
				});
		dto.setStatut(x.getStatut());
		dto.setDateCreation(x.getDateCreation());
		dto.setDateDesactivation(x.getDateDesactivation());
		dto.setDateModification(x.getDateModification());
		return dto;
	}

	private ListAttributAUTO mapToList(Cumpus x) {
		ListAttributAUTO dto = new ListAttributAUTO();
		dto.setId(x.getId());
		dto.setLibelle(x.getNom());

		return dto;
	}

}
