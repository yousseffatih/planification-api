package com.WALID.planification_api.playload.DTO;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanificationsDTO {

	    private Long id;

	    @NotNull(message = "Nom est obligatoire!")
		@NotEmpty(message = "Nom est obligatoire!")
	    private String nom;

	    @JsonFormat(pattern = "dd/MM/yyyy")
	    private Date dateCreation;

	    @JsonFormat(pattern = "dd/MM/yyyy")
	    private Date dateDesactivation;

	    @JsonFormat(pattern = "dd/MM/yyyy")
	    private Date dateModification;

	    private String statut;

	    @JsonFormat(pattern = "dd/MM/yyyy")
	    @NotNull(message = "Le nom du professeur est obligatoire !")
		private Date datePlanification;

	    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	    @NotNull(message = "L'heure de debut est obligatoire !")
		private LocalDateTime timeDebut;

	    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	    @NotNull(message = "L'heure de fin est obligatoire !")
		private LocalDateTime timeFin;

		private String prof;
	    
	    
	    @NotNull(message = "Prof est obligatoire !")
		private Long idProfesseur;
	    private String libelleProfeseur;

	    @NotNull(message = "Module est obligatoire !")
		private Long idModule;
		private String libelleModule;

		@NotNull(message = "Salle est obligatoire !")
		private Long idSalle;
		private String libelleSalle;
		
		@NotNull(message = "Utilisateur est obligatoire !")
		private Long idUser;
		private String libelleUser;
		
		@NotNull(message = "Type Planification est obligatoire !")
		private Long idTypePlanification;
		private String libelleTypePlanification;

		private String description;

		@NotNull(message = "Les classes est obligatoire !")
		@NotEmpty(message = "Au moins une classe est obligatoire !")
		private List<Long> idsClasses;
}
