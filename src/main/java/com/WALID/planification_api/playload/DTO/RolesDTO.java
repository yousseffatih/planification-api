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
public class RolesDTO {
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
}
