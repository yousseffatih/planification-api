package com.WALID.planification_api.services.Roles;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.WALID.planification_api.constants.GlobalConstant;
import com.WALID.planification_api.entities.Roles;
import com.WALID.planification_api.playload.ResourceNotFoundException;
import com.WALID.planification_api.playload.DTO.RolesDTO;
import com.WALID.planification_api.repositories.Parametrage.RolesRepository;


@Service
public class RolesServicesImp implements InRolesServices{

	@Autowired
	private RolesRepository rolesRepository;
	
	@Override
	public List<RolesDTO> getAllRoles() {
		List<Roles> roles = rolesRepository.findAllWithStatus();
        return  roles.stream().map((c) -> mapToDTO(c)).collect(Collectors.toList());
	}

	@Override
	public RolesDTO getRoleById(Long id) {
		Roles role = rolesRepository.findByIdStatut(id).orElseThrow(() -> new ResourceNotFoundException("Role","id",id));
        return  mapToDTO(role);
	}

	@Override
	public RolesDTO addRole(RolesDTO rolesDTO) {
		Roles role = new Roles();

		role.setDateCreation(new Date());
		role.setStatut(GlobalConstant.STATUT_ACTIF);
		role.setNom(rolesDTO.getNom());
		Roles rl = rolesRepository.save(role);
	    return mapToDTO(rl);
	}

	@Override
	public void deleteRole(Long id) {
		Roles role = rolesRepository.findByIdStatut(id).orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
		role.setDateDesactivation(new Date());
		role.setStatut(GlobalConstant.STATUT_DELETE);
        rolesRepository.save(role);

	}

	@Override
	public RolesDTO updateRole(Long id, RolesDTO modulesDTO) {
		Roles role = rolesRepository.findByIdStatut(id).orElseThrow(()-> new ResourceNotFoundException("Role", "id", id));

		role.setNom(modulesDTO.getNom());
    	//classe.setStatut(classesDTO.getStatut());
		role.setDateModification(new Date());

		Roles rl = rolesRepository.save(role);
		return mapToDTO(rl);
	}

	private RolesDTO mapToDTO(Roles x)
    {
		RolesDTO dto = new RolesDTO();
        dto.setId(x.getId());
        dto.setNom(x.getNom());

        dto.setStatut(x.getStatut());
        dto.setDateCreation(x.getDateCreation());
        dto.setDateDesactivation(x.getDateDesactivation());
        dto.setDateModification(x.getDateModification());
        return dto;
    }

}
