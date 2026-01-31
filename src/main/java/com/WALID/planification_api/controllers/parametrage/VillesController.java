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
import com.WALID.planification_api.playload.DTO.VillesDTO;
import com.WALID.planification_api.services.Villes.InVillesServices;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/villes")
@RequiredArgsConstructor
public class VillesController {

    @Autowired
    private InVillesServices villesServices;

    @GetMapping("")
    public ResponseEntity<List<VillesDTO>> getAllResponseEntity() {
        List<VillesDTO> villesDtos = villesServices.getAllVilles();
        return new ResponseEntity<>(villesDtos, HttpStatus.OK);
    }

    @GetMapping("/listVilles")
    public ResponseEntity<List<ListAttributAUTO>> getListVilles() {
        List<ListAttributAUTO> vilAttributAUTOs = villesServices.getVillesListApi();
        return new ResponseEntity<>(vilAttributAUTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VillesDTO> getClasseById(@PathVariable Long id) {

        VillesDTO villeDTO = villesServices.getVilleById(id);
        return new ResponseEntity<>(villeDTO, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> addClasse(@Valid @RequestBody VillesDTO villesDTO) {

        villesServices.addVille(villesDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Ville ajoutée.", "success"));
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteClasse(@PathVariable Long id, @RequestParam Map<String, String> motif) {
        if (motif.get("motif") == null || motif.get("motif").isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Motif manquant.", "error"));
        }
        villesServices.deleteVille(id, motif.get("motif"));
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Ville supprimée.", "success"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClasses(@PathVariable Long id, @Valid @RequestBody VillesDTO villesDTO) {
        // if(villesDTO.getStatut().equals(GlobalConstant.STATUT_INACTIF) &&
        // (villesDTO.getMotif().isEmpty() || villesDTO.getMotif() == null))
        // {
        // ResponseEntity.status(GlobalConstant.HTTPSTATUT_RESPONSE_ERORR) .body(new
        // MessageResponse("Veuillez entrer un motif pour ce statut.", "warning"));
        // }
        villesServices.updateVille(id, villesDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Ville modifiée.", "success"));

    }

}
