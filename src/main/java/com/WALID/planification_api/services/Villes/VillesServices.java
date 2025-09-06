package com.WALID.planification_api.services.Villes;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.WALID.planification_api.constants.GlobalConstant;
import com.WALID.planification_api.entities.Villes;
import com.WALID.planification_api.playload.ResourceNotFoundException;
import com.WALID.planification_api.playload.DTO.ListAttributAUTO;
import com.WALID.planification_api.playload.DTO.VillesDTO;
import com.WALID.planification_api.repositories.Parametrage.VillesRepository;


@Service
public class VillesServices implements InVillesServices{
	
	@Autowired
	private VillesRepository villeRepository;

	@Override
	public List<VillesDTO> getAllVilles() {
		List<Villes> villes = villeRepository.findAllWithStatus();
        return  villes.stream().map((c) -> mapToDTO(c)).collect(Collectors.toList());
	}

	@Override
	public List<ListAttributAUTO> getVillesListApi() {
		List<Villes> villes = villeRepository.getClassesListApi();
        return  villes.stream().map((c) -> mapToList(c)).collect(Collectors.toList());
	}

	@Override
	public VillesDTO getVilleById(Long id) {
		 Villes ville = villeRepository.findByIdStatut(id).orElseThrow(() -> new ResourceNotFoundException("Ville","id",id));
	     return  mapToDTO(ville);
	}

	@Override
	public VillesDTO addVille(VillesDTO villesDTO) {
		Villes ville = new Villes();

		ville.setDateCreation(new Date());
		ville.setStatut(GlobalConstant.STATUT_ACTIF);
		ville.setNom(villesDTO.getNom());
		ville.setLibelle(GlobalConstant.formatName(villesDTO.getNom()));
        return mapToDTO(ville);
	}

	@Override
	public void deleteVille(Long id) {
		 Villes ville = villeRepository.findByIdStatut(id).orElseThrow(() -> new ResourceNotFoundException("Ville","id",id));
		 ville.setStatut(GlobalConstant.STATUT_DELETE);
		 ville.setDateDesactivation(new Date());
	     villeRepository.save(ville);
	}

	@Override
	public VillesDTO updateVille(Long idLong, VillesDTO villesDTO) {
		Villes ville = villeRepository.findByIdStatut(idLong).orElseThrow(() -> new ResourceNotFoundException("Ville","id",idLong));
		ville.setDateModification(new Date());
		ville.setStatut(villesDTO.getStatut());
		ville.setMotif(villesDTO.getMotif());
		ville.setNom(villesDTO.getNom());
		ville.setLibelle(GlobalConstant.formatName(villesDTO.getNom()));
		return null;
	}
	
	
	private VillesDTO mapToDTO(Villes x)
    {
		VillesDTO dto = new VillesDTO();
        dto.setId(x.getId());
        dto.setNom(x.getNom());
        dto.setMotif(x.getMotif());
       

        dto.setStatut(x.getStatut());
        dto.setDateCreation(x.getDateCreation());
        dto.setDateDesactivation(x.getDateDesactivation());
        dto.setDateModification(x.getDateModification());
        return dto;
    }
    
    private ListAttributAUTO mapToList(Villes x)
    {
		ListAttributAUTO dto = new ListAttributAUTO();
        dto.setId(x.getId());
        dto.setLibelle(x.getNom());

        return dto;
    }
   
}
