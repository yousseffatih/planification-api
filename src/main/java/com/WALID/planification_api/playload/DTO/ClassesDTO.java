package com.WALID.planification_api.playload.DTO;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClassesDTO {

    private Long id;

    @NotNull(message = "Nom est obligatoire!")
    @NotEmpty(message = "Nom est obligatoire!")
    @Size(max = 20, message = "Le nom ne peut pas dépasser 20 caractères")
    private String nom;

    @NotNull(message = "Année scolaire est obligatoire!")
    @NotEmpty(message = "Année scolaire est obligatoire!")
    @Size(min = 5, max = 5, message = "L'année scolaire doit avoir une longueur de 5 caractères")
    @Pattern(regexp = "\\d{2}/\\d{2}", message = "L'année scolaire doit être au format YY/YY (ex: 23/24)")
    private String annuerScolaire;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateCreation;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateDesactivation;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateModification;

    private String statut;

    @NotNull(message = "Nombre d'étudiants est obligatoire!")
    @Min(value = 0, message = "Le nombre d'étudiants ne peut pas être négatif")
    @Max(value = 9999, message = "Le nombre d'étudiants ne peut pas dépasser 9999")
    private Integer nomberEff;

    private String libelle;

}
