package com.WALID.planification_api.playload.DTO;

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
public class ProfesseurDTO {
	
	 	private Long id;
	 
	    @NotNull(message = "Nom est obligatoire!")
		@NotEmpty(message = "Nom est obligatoire!")
	    private String nom;

	    @NotNull(message = "Prénom est obligatoire!")
		@NotEmpty(message = "Prénom est obligatoire!")
	    private String  prenom;
	    
	    @NotNull(message = "Email est obligatoire!")
		@NotEmpty(message = "Email est obligatoire!")
	    private String email;
	    
	    @NotNull(message = "Numéro est obligatoire!")
		@NotEmpty(message = "Numéro est obligatoire!")
	    private String numeroTele;

	    @JsonFormat(pattern = "dd/MM/yyyy")
	    private Date dateCreation;

	    @JsonFormat(pattern = "dd/MM/yyyy")
	    private Date dateDesactivation;

	    @JsonFormat(pattern = "dd/MM/yyyy")
	    private Date dateModification;

	    private String statut;
	    
	    @NotNull(message = "Type de professeur est obligatoire!")
	    private Long idTypeProf;

		private String libelleTypeProf;
}
