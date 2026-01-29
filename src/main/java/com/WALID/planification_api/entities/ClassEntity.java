package com.WALID.planification_api.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class ClassEntity {

	@Size(max = 20)
	@Column(name = "STATUT", length = 20)
	protected String statut;

	@Column(name = "DATE_CREATION")
	protected Date dateCreation;

	@Column(name = "DATE_DESACTIVATION")
	protected Date dateDesactivation;

	@Column(name = "DATE_MODIFICATION")
	protected Date dateModification;

	@Column(name = "MOTIF")
	protected String motif;

	@Column(name = "LIBELLE")
	protected String libelle;

}