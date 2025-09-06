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
import com.WALID.planification_api.playload.DTO.ClassesDTO;
import com.WALID.planification_api.playload.DTO.ListAttributAUTO;
import com.WALID.planification_api.repositories.Parametrage.ClassesRepository;
import com.WALID.planification_api.services.Classes.InClassesServices;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class ClassesController {

    @Autowired
    private InClassesServices classesServices;

    @Autowired
    private ClassesRepository classesRepository;


    @GetMapping("")
    public ResponseEntity<List<ClassesDTO>> getAllResponseEntity()
    {
        List<ClassesDTO> classesDTOs = classesServices.getAllClasses();
        return new ResponseEntity<>(classesDTOs , HttpStatus.OK);
    }
    
    @GetMapping("/listClasses")
    public ResponseEntity<List<ListAttributAUTO>> getListModules()
    {
        List<ListAttributAUTO> modulesDTOs = classesServices.getClassesListApi();
        return new ResponseEntity<>(modulesDTOs , HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ClassesDTO> getClasseById(@PathVariable Long id) {

        ClassesDTO classesDTO = classesServices.getClasseById(id);
        return new ResponseEntity<>(classesDTO,HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> addClasse(@Valid @RequestBody ClassesDTO classesDTO) {
    	boolean ifNomExist = classesRepository.existsByNomAndStatut(classesDTO.getNom(), GlobalConstant.STATUT_ACTIF);
    	if(ifNomExist)
    	{
    		return ResponseEntity.status(GlobalConstant.HTTPSTATUT_RESPONSE_ERORR).body(new MessageResponse("Le nom existe déjà !", "warning") );
    	}
    	
    	classesServices.addClasse(classesDTO);
    	return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Classe ajoutée.","success"));
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteClasse(@PathVariable Long id) {
        classesServices.deleteClasse(id);
        return  ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Classe supprimée.","success"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClasses(@PathVariable Long id,@Valid @RequestBody ClassesDTO classesDTO)
    {
    	boolean ifNomModifExists = classesRepository.existsByNomModif(classesDTO.getNom(),id);
    	if(ifNomModifExists)
    	{
    		return ResponseEntity.status(GlobalConstant.HTTPSTATUT_RESPONSE_ERORR).body(new MessageResponse("Le nom existe déjà !", "warning") );
    	}
    	classesServices.updateClasse(id,classesDTO);
    	return  ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Classe modifiée.","success"));

    }



}
