package com.WALID.planification_api.controllers.parametrage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.WALID.planification_api.constants.GlobalConstant;
import com.WALID.planification_api.playload.MessageResponse;
import com.WALID.planification_api.playload.DTO.ListAttributAUTO;
import com.WALID.planification_api.playload.DTO.ModulesDTO;
import com.WALID.planification_api.repositories.Parametrage.ModulesRepository;
import com.WALID.planification_api.services.Modules.InModulesServices;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
public class ModulesController {

	@Autowired
	private InModulesServices modulesServices;

	@Autowired
	private ModulesRepository modulesRepository;

	@GetMapping("")
    public ResponseEntity<List<ModulesDTO>> getAllModules()
    {
        List<ModulesDTO> modulesDTOs = modulesServices.getAllModules();
        return new ResponseEntity<>(modulesDTOs , HttpStatus.OK);
    }
	
	@GetMapping("/listModules")
    public ResponseEntity<List<ListAttributAUTO>> getListModules()
    {
        List<ListAttributAUTO> modulesDTOs = modulesServices.getModulesListApi();
        return new ResponseEntity<>(modulesDTOs , HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModulesDTO> getClasseById(@PathVariable Long id) {

    	ModulesDTO modulesDTO = modulesServices.getModuleById(id);
        return new ResponseEntity<>(modulesDTO,HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> addClasse(@Valid @RequestBody ModulesDTO modulesDTO) {
    	boolean ifNomExist = modulesRepository.existsByNomAndStatut(modulesDTO.getNom(), GlobalConstant.STATUT_ACTIF);
    	if(ifNomExist)
    	{
    		return ResponseEntity.status(GlobalConstant.HTTPSTATUT_RESPONSE_ERORR).body(new MessageResponse("Le nom existe déjà !", "warning") );
    	}
    	modulesServices.addModule(modulesDTO);
    	return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Module ajoutée.","success"));
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteClasse(@PathVariable Long id) {
        modulesServices.deleteModule(id);
        return  ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Module supprimée.","success"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClasses(@PathVariable Long id,@Valid @RequestBody ModulesDTO modulesDTO)
    {
    	boolean ifNomModifExists = modulesRepository.existsByNomModif(modulesDTO.getNom(),id);
    	if(ifNomModifExists)
    	{
    		return ResponseEntity.status(GlobalConstant.HTTPSTATUT_RESPONSE_ERORR).body(new MessageResponse("Le nom existe déjà !", "warning") );
    	}
    	modulesServices.updateModule(id,modulesDTO);
    	return  ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Module modifiée.","success"));

    }
}
