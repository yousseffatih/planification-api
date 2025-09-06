package com.WALID.planification_api.entities;


import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PLANIFICATIONS")
public class Planifications extends ClassEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nom;

	@Column(name = "DATE_PLANIFICATION")
	private Date datePlanification;

	@Column(name = "TIME_DEBUT")
	private LocalDateTime timeDebut;

	@Column(name = "TIME_FIN")
	private LocalDateTime timeFin;

	@Size(max = 50)
	@Column(name = "PROF", length = 50)
	private String prof;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="PROFESSEUR_ID")
	private Professeur professeur;
	
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="MODULE_ID")
	private Modules module;

	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="SALLE_ID")
	private Salles salle;

	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="USER_ID")
	private Users user;
	
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="Type_planification_ID")
	private ListAttribut typePlanification;
	
	
	@Column(name = "DESCRIPTION")
	@Size(max = 300)
	private String description;
}
