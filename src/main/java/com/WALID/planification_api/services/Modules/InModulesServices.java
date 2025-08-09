package com.WALID.planification_api.services.Modules;

import java.util.List;

import com.WALID.planification_api.playload.DTO.ModulesDTO;


public interface InModulesServices {

	public List<ModulesDTO> getAllModules();

    public ModulesDTO getModuleById(Long id);

    public ModulesDTO addModule(ModulesDTO modulesDTO);

    public void deleteModule(Long id);

    public ModulesDTO updateModule(Long idLong , ModulesDTO modulesDTO);
}
