package com.WALID.planification_api.playload.DTO;

import java.util.Date;
//import java.util.Set;

//import com.WALID.planification_api.entities.Roles;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersDTO {

    private Long id;

    private String statut;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateCreation;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateModification;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateDesactivation;

    @NotNull(message = "Username est obligatoire!")
    @NotEmpty(message = "Username est obligatoire!")
    private String username;

    private String password;

    @Email
    private String email;

    private String first;

    @NotNull(message = "Nom est obligatoire!")
    @NotEmpty(message = "Nom est obligatoire!")
    private String nom;

    @NotNull(message = "Prénom est obligatoire!")
    @NotEmpty(message = "Prénom est obligatoire!")
    private String prenom;

    // private Set<Roles> roles;

    @NotNull(message = "Role est obligatoire!")
    private Long idRole;
    private String libelleRole;

    private Long idCumpus;
    private String libelleCumpus;

}
