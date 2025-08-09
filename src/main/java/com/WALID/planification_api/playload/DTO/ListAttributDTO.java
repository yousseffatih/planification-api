package com.WALID.planification_api.playload.DTO;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListAttributDTO {

	private Long id;

    @NotNull(message = "Libelle est obligatoire!")
	@NotEmpty(message = "Libelle est obligatoire!")
    private String libelle;

    @NotNull(message = "Value est obligatoire!")
	@NotEmpty(message = "Value est obligatoire!")
    @Pattern(regexp = "^[^\\s]+$", message = "Value ne doit pas contenir d'espaces!")
    private String value;


    @NotNull(message = "Nom de API est obligatoire!")
	@NotEmpty(message = "Nom de API est obligatoire!")
    private String listNameApi;


    @NotNull(message = "List Libelle de API est obligatoire!")
	@NotEmpty(message = "List Libelle de API est obligatoire!")
    private String listLibelle;


    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateCreation;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateDesactivation;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateModification;

    private String statut;
}
