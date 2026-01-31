package com.WALID.planification_api.controllers.parametrage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

import com.WALID.planification_api.constants.GlobalConstant;
import com.WALID.planification_api.playload.MessageResponse;
import com.WALID.planification_api.playload.DTO.AvailableSalleBody;
import com.WALID.planification_api.playload.DTO.ListAttributAUTO;
import com.WALID.planification_api.playload.DTO.SallesDTO;
import com.WALID.planification_api.repositories.Parametrage.SallesRepository;
import com.WALID.planification_api.services.Salles.InSallesServices;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/salles")
@RequiredArgsConstructor
public class SallesController {
	@Autowired
	private InSallesServices sallesServices;

	@Autowired
	private SallesRepository sallesRepository;

	@GetMapping("")
	public ResponseEntity<List<SallesDTO>> getAllSalles() {
		List<SallesDTO> rolesDTOs = sallesServices.getAllSalles();
		return new ResponseEntity<>(rolesDTOs, HttpStatus.OK);
	}

	@GetMapping("/listSalles")
	public ResponseEntity<List<ListAttributAUTO>> getAllSallesApi() {
		List<ListAttributAUTO> list = sallesServices.gettAllSallesApi();
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@GetMapping("/ava")
	public ResponseEntity<List<SallesDTO>> getAllSallesAvailab(@RequestParam("date") String dateStr) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date date = dateFormat.parse(dateStr);
			List<SallesDTO> sallesDTOs = new ArrayList<>();
			// List<SallesDTO> rolesDTOs = sallesServices.availableSalles(date);
			return new ResponseEntity<>(sallesDTOs, HttpStatus.OK);
		} catch (ParseException e) {
			return ResponseEntity.badRequest().body(Collections.emptyList());
		}
	}

	@GetMapping("/availability")
	public ResponseEntity<List<SallesDTO>> getAllSallesAvailability(
			@Valid @RequestBody AvailableSalleBody availableSalleBody) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date date = dateFormat.parse(availableSalleBody.getDatePlanification());
			List<SallesDTO> sallesDTOs = sallesServices.availableSalles(date, availableSalleBody.getTimeStarTime(),
					availableSalleBody.getTimeEnDateTime());
			return new ResponseEntity<>(sallesDTOs, HttpStatus.OK);
		} catch (ParseException e) {
			return ResponseEntity.badRequest().body(Collections.emptyList());
		}
	}

	@GetMapping("/availability/{id}")
	public ResponseEntity<List<SallesDTO>> getAllSallesAvailabilityModif(
			@Valid @RequestBody AvailableSalleBody availableSalleBody, @PathVariable Long id) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date date = dateFormat.parse(availableSalleBody.getDatePlanification());
			List<SallesDTO> sallesDTOs = sallesServices.availableSallesModif(date, availableSalleBody.getTimeStarTime(),
					availableSalleBody.getTimeEnDateTime(), id);
			return new ResponseEntity<>(sallesDTOs, HttpStatus.OK);
		} catch (ParseException e) {
			return ResponseEntity.badRequest().body(Collections.emptyList());
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<SallesDTO> getRoleById(@PathVariable Long id) {

		SallesDTO sallesDTO = sallesServices.getSalleById(id);
		return new ResponseEntity<>(sallesDTO, HttpStatus.OK);
	}

	@PostMapping("")
	public ResponseEntity<?> addClasse(@Valid @RequestBody SallesDTO sallesDTO) {
		boolean ifNomExist = sallesRepository.existsByNom(sallesDTO.getNom());
		if (ifNomExist) {
			return ResponseEntity.status(GlobalConstant.HTTPSTATUT_RESPONSE_ERORR)
					.body(new MessageResponse("Le nom existe déjà !", "warning"));
		}

		sallesServices.addSalle(sallesDTO);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Salle ajoutée.", "success"));
	}

	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteClasse(@PathVariable Long id, @RequestBody Map<String, String> motif) {
		if (motif.get("motif") == null || motif.get("motif").isEmpty()) {
			return ResponseEntity.status(GlobalConstant.HTTPSTATUT_RESPONSE_ERORR)
					.body(new MessageResponse("Le motif est requis !", "warning"));
		}
		sallesServices.deleteSalle(id, motif.get("motif"));
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Salle supprimée.", "success"));
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateClasses(@PathVariable Long id, @Valid @RequestBody SallesDTO sallesDTO) {
		boolean ifNomModifExists = sallesRepository.existsByNomModif(sallesDTO.getNom(), id);
		if (ifNomModifExists) {
			return ResponseEntity.status(GlobalConstant.HTTPSTATUT_RESPONSE_ERORR)
					.body(new MessageResponse("Le nom existe déjà !", "warning"));
		}
		sallesServices.updateSalle(id, sallesDTO);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Salle modifiée.", "success"));

	}
}
