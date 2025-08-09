package com.WALID.planification_api.controllers.parametrage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.WALID.planification_api.constants.GlobalConstant;
import com.WALID.planification_api.playload.MessageResponse;
import com.WALID.planification_api.playload.DTO.ListAttributAUTO;
import com.WALID.planification_api.playload.DTO.ListAttributDTO;
import com.WALID.planification_api.repositories.Parametrage.ListAttributRepository;
import com.WALID.planification_api.services.ListAttribut.InListAttributeServices;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/attributs")
@RequiredArgsConstructor
public class ListAttributController {

	@Autowired
	private InListAttributeServices listAttributeServices;

	@Autowired
	private ListAttributRepository listAttributRepository;

	@PostMapping("")
    public ResponseEntity<?> addClasse(@Valid @RequestBody ListAttributDTO listAttribut) {
		boolean ifLibelleExist = listAttributRepository.existsByLibelleAndStatut(listAttribut.getLibelle(), GlobalConstant.STATUT_ACTIF);
    	if(ifLibelleExist)
    	{
    		return ResponseEntity.status(GlobalConstant.HTTPSTATUT_RESPONSE_ERORR).body(new MessageResponse("Libelle existe déjà !", "warning") );
    	}
		listAttributeServices.addListAttribute(listAttribut);
    	return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Ajouté avec succès.","success"));
    }

	@GetMapping("/listTypeSalles")
	public ResponseEntity<List<ListAttributAUTO>> listTypeSalles()
	{
		List<ListAttributAUTO> list= listAttributeServices.getListAttributes("listTypeSalles");
		return new ResponseEntity<>(list , HttpStatus.OK);
   }

   @GetMapping("/listTypePlanification")
	public ResponseEntity<List<ListAttributAUTO>> listTypeTypePlanification()
	{
		List<ListAttributAUTO> list= listAttributeServices.getListAttributes("listTypePlanification");
		return new ResponseEntity<>(list , HttpStatus.OK);
   }
	
	@GetMapping("/listTypeProfesseurs")
	public ResponseEntity<List<ListAttributAUTO>> listTypeProfess()
	{
		List<ListAttributAUTO> list= listAttributeServices.getListAttributes("listTypeProfesseurs");
		return new ResponseEntity<>(list , HttpStatus.OK);
   }
}