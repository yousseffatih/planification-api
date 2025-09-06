package com.WALID.planification_api.playload.DTO;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VillesDTO {
	
    private Long id;

    @NotNull(message = "Nom est obligatoire!")
	@NotEmpty(message = "Nom est obligatoire!")
    @Size(max = 30, message = "Le nom ne peut pas dépasser 30 caractères")
    private String nom;


    private String libelle;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateCreation;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateDesactivation;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateModification;

    private String statut;
    
    private String motif;
}
