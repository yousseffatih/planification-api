package com.WALID.planification_api.services.Roles;

import java.util.List;

import com.WALID.planification_api.playload.DTO.RolesDTO;


public interface InRolesServices {
	public List<RolesDTO> getAllRoles();

    public RolesDTO getRoleById(Long id);

    public RolesDTO addRole(RolesDTO modulesDTO);

    public void deleteRole(Long id);

    public RolesDTO updateRole(Long idLong , RolesDTO modulesDTO);
}
