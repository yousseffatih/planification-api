package com.WALID.planification_api.playload.DTO;

import java.time.LocalDateTime;
import java.util.Date;

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
public class ListPlanifications {

	     Long getId;

	    @NotNull(message = "Nom est obligatoire!")
		@NotEmpty(message = "Nom est obligatoire!")
	     String getNom;

	    @JsonFormat(pattern = "dd/MM/yyyy")
	     Date getDateCreation;

	    @JsonFormat(pattern = "dd/MM/yyyy")
	     Date getDateDesactivation;

	    @JsonFormat(pattern = "dd/MM/yyyy")
	     Date getDateModification;

	     String getStatut;

	    @JsonFormat(pattern = "dd/MM/yyyy")
		 Date getDatePlanification;

		 LocalDateTime getTimeDebut;

		 LocalDateTime getTimeFin;

		 String getProf;

		 Long getIdModule;
		 String getLibelleModule;

		 Long getIdSalle;
		 String getLibelleSalle;

		 Long getIdUser;
		 String getLibelleUser;
}
