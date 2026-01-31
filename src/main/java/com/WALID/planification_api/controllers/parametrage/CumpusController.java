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
import org.springframework.web.bind.annotation.RestController;

import com.WALID.planification_api.constants.GlobalConstant;
import com.WALID.planification_api.playload.MessageResponse;
import com.WALID.planification_api.playload.DTO.CumpusDTO;
import com.WALID.planification_api.playload.DTO.ListAttributAUTO;
import com.WALID.planification_api.services.Cumpus.InCumpusService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cumpus")
@RequiredArgsConstructor
public class CumpusController {

    @Autowired
    private InCumpusService cumpusService;

    @GetMapping("")
    public ResponseEntity<List<CumpusDTO>> getAllResponseEntity() {
        List<CumpusDTO> cumpusDTOs = cumpusService.getAllCumpus();
        return new ResponseEntity<>(cumpusDTOs, HttpStatus.OK);
    }

    @GetMapping("/listCumpus")
    public ResponseEntity<List<ListAttributAUTO>> getListVilles() {
        List<ListAttributAUTO> cumAttributAUTOs = cumpusService.getCumpusListApi();
        return new ResponseEntity<>(cumAttributAUTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CumpusDTO> getClasseById(@PathVariable Long id) {

        CumpusDTO cumpusDTO = cumpusService.getCumpusById(id);
        return new ResponseEntity<>(cumpusDTO, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> addClasse(@Valid @RequestBody CumpusDTO cumpusDTO) {

        cumpusService.addCumpus(cumpusDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Cumpus ajoutée.", "success"));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteClasse(@PathVariable Long id, @RequestBody Map<String, String> motif) {
        if (motif.get("motif") == null || motif.get("motif").isEmpty()) {
            return ResponseEntity.status(GlobalConstant.HTTPSTATUT_RESPONSE_ERORR)
                    .body(new MessageResponse("Le motif est obligatoire !", "warning"));
        }
        cumpusService.deleteCumpus(id, motif.get("motif"));
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Cumpus supprimée.", "success"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClasses(@PathVariable Long id, @Valid @RequestBody CumpusDTO cumpusDTO) {
        cumpusService.updateCumpus(id, cumpusDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Cumpus modifiée.", "success"));

    }
}
