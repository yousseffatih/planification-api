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
import com.WALID.planification_api.playload.DTO.RolesDTO;
import com.WALID.planification_api.repositories.Parametrage.RolesRepository;
import com.WALID.planification_api.services.Roles.InRolesServices;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolesController {

	@Autowired
	private InRolesServices rolesServices;

	@Autowired
	private RolesRepository rolesRepository;

	@GetMapping("")
    public ResponseEntity<List<RolesDTO>> getAllRoles()
    {
        List<RolesDTO> rolesDTOs = rolesServices.getAllRoles();
        return new ResponseEntity<>(rolesDTOs , HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolesDTO> getRoleById(@PathVariable Long id) {

    	RolesDTO rolesDTO = rolesServices.getRoleById(id);
        return new ResponseEntity<>(rolesDTO,HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> addClasse(@Valid @RequestBody RolesDTO rolesDTO) {
    	boolean ifNomExist = rolesRepository.existsByNomAndStatut(rolesDTO.getNom(), GlobalConstant.STATUT_ACTIF);
    	if(ifNomExist)
    	{
    		return ResponseEntity.status(GlobalConstant.HTTPSTATUT_RESPONSE_ERORR).body(new MessageResponse("Le nom existe déjà !", "warning") );
    	}
    	rolesServices.addRole(rolesDTO);
    	return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Role ajoutée.","success"));
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteClasse(@PathVariable Long id) {
    	rolesServices.deleteRole(id);
        return  ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Role supprimée.","success"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClasses(@PathVariable Long id,@Valid @RequestBody RolesDTO rolesDTO)
    {
    	boolean ifNomModifExists = rolesRepository.existsByNomModif(rolesDTO.getNom(),id);
    	if(ifNomModifExists)
    	{
    		return ResponseEntity.status(GlobalConstant.HTTPSTATUT_RESPONSE_ERORR).body(new MessageResponse("Le nom existe déjà !", "warning") );
    	}
    	rolesServices.updateRole(id,rolesDTO);
    	return  ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Role modifiée.","success"));

    }
}
