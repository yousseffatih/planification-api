package com.WALID.planification_api.services.Modules;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.WALID.planification_api.constants.GlobalConstant;
import com.WALID.planification_api.entities.Modules;
import com.WALID.planification_api.entities.Salles;
import com.WALID.planification_api.playload.ResourceNotFoundException;
import com.WALID.planification_api.playload.DTO.ListAttributAUTO;
import com.WALID.planification_api.playload.DTO.ModulesDTO;
import com.WALID.planification_api.repositories.Parametrage.ModulesRepository;



@Service
public class ModulesServicesImp implements InModulesServices{

	@Autowired
	private ModulesRepository modulesRepository;

	@Override
	public List<ModulesDTO> getAllModules() {
		List<Modules> modules = modulesRepository.findAllWithStatus();
        return  modules.stream().map((c) -> mapToDTO(c)).collect(Collectors.toList());
	}
	
	@Override
	public List<ListAttributAUTO> getModulesListApi() {
		List<Modules> modules = modulesRepository.findAllWithStatusListApi();
        return  modules.stream().map((c) -> mapToList(c)).collect(Collectors.toList());
	}

	@Override
	public ModulesDTO getModuleById(Long id) {
		Modules module = modulesRepository.findByIdStatut(id).orElseThrow(() -> new ResourceNotFoundException("Module","id",id));
        return  mapToDTO(module);
	}

	@Override
	public ModulesDTO addModule(ModulesDTO moduleDto) {
		Modules module = new Modules();

		module.setDateCreation(new Date());
		module.setStatut(GlobalConstant.STATUT_ACTIF);
		module.setNom(moduleDto.getNom());
		Modules mdl = modulesRepository.save(module);
	    return mapToDTO(mdl);
	}

	@Override
	public void deleteModule(Long id) {
		Modules mdl = modulesRepository.findByIdStatut(id).orElseThrow(() -> new ResourceNotFoundException("Module", "id", id));
        mdl.setDateDesactivation(new Date());
        mdl.setStatut(GlobalConstant.STATUT_DELETE);
        modulesRepository.save(mdl);

	}

	@Override
	public ModulesDTO updateModule(Long id, ModulesDTO modulesDTO) {
		Modules module = modulesRepository.findByIdStatut(id).orElseThrow(()-> new ResourceNotFoundException("Module", "id", id));

		module.setNom(modulesDTO.getNom());
    	module.setStatut(modulesDTO.getStatut());
		module.setDateModification(new Date());

    	Modules mdl = modulesRepository.save(module);
		return mapToDTO(mdl);
	}

	private ModulesDTO mapToDTO(Modules x)
    {
		ModulesDTO dto = new ModulesDTO();
        dto.setId(x.getId());
        dto.setNom(x.getNom());

        dto.setStatut(x.getStatut());
        dto.setDateCreation(x.getDateCreation());
        dto.setDateDesactivation(x.getDateDesactivation());
        dto.setDateModification(x.getDateModification());
        return dto;
    }
	
	private ListAttributAUTO mapToList(Modules x)
    {
		ListAttributAUTO dto = new ListAttributAUTO();
        dto.setId(x.getId());
        dto.setLibelle(x.getNom());

        return dto;
    }
}
