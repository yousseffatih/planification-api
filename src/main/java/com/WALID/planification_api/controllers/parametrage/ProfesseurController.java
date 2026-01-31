package com.WALID.planification_api.controllers.parametrage;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.WALID.planification_api.playload.MessageResponse;
import com.WALID.planification_api.playload.DTO.ListAttributAUTO;
import com.WALID.planification_api.playload.DTO.ProfesseurDTO;
import com.WALID.planification_api.services.Professeur.InProfesseurServices;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/professeurs")
@RequiredArgsConstructor
public class ProfesseurController {

    @Autowired
    private InProfesseurServices professeurServices;

    @GetMapping("")
    public ResponseEntity<List<ProfesseurDTO>> getAllResponseEntity() {
        List<ProfesseurDTO> classesDTOs = professeurServices.AllProfesseur();
        return new ResponseEntity<>(classesDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfesseurDTO> getClasseById(@PathVariable Long id) {

        ProfesseurDTO classesDTO = professeurServices.getProfesseurById(id);
        return new ResponseEntity<>(classesDTO, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> addClasse(@Valid @RequestBody ProfesseurDTO classesDTO) {
        // boolean ifNomExist =
        // professeurServices.existsByNomAndStatut(classesDTO.getNom(),
        // GlobalConstant.STATUT_ACTIF);
        // if(ifNomExist)
        // {
        // return
        // ResponseEntity.status(GlobalConstant.HTTPSTATUT_RESPONSE_ERORR).body(new
        // MessageResponse("Le nom existe déjà !", "warning") );
        // }

        professeurServices.addProfesseur(classesDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Prof ajoutée.", "success"));
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteClasse(@PathVariable Long id, @RequestParam Map<String, String> motif) {
        if (motif.get("motif") == null || motif.get("motif").isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Motif manquant.", "error"));
        }
        professeurServices.deleteProfesseur(id, motif.get("motif"));
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Prof supprimée.", "success"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClasses(@PathVariable Long id, @Valid @RequestBody ProfesseurDTO classesDTO) {
        // boolean ifNomExist =
        // professeurServices.existsByNomAndStatut(classesDTO.getNom(),
        // GlobalConstant.STATUT_ACTIF);
        // if(ifNomExist)
        // {
        // return
        // ResponseEntity.status(GlobalConstant.HTTPSTATUT_RESPONSE_ERORR).body(new
        // MessageResponse("Le nom existe déjà !", "warning") );
        // }
        professeurServices.updateProfesseur(id, classesDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Classe modifiée.", "success"));

    }

    @GetMapping("/listProfesseurs")
    public ResponseEntity<List<ListAttributAUTO>> getAllSallesApi() {
        List<ListAttributAUTO> list = professeurServices.gettAllSallesApi();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
