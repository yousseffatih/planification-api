package com.WALID.planification_api.entities;

import org.hibernate.sql.results.NoMoreOutputsException;

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
@Table(name = "CUMPUS")
public class Cumpus extends ClassEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	

	@Size(max = 20)
	@Column(name = "NOM", length = 20)
	private String nom;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="VILLE_ID")
	private Villes ville;
}
