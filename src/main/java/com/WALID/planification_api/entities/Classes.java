package com.WALID.planification_api.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CLASSES")
public class Classes extends ClassEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(max = 20)
	@Column(name = "NOM", length = 20)
	private String nom;

	@Size(max = 20)
	@Column(name = "ANNUER_SCOLAIRE", length = 20)
	private String annuerScolaire;

	
	@Min(value = 0, message = "Le nombre d'étudiants ne peut pas être négatif")
	@Max(value = 9999, message = "Le nombre d'étudiants ne peut pas dépasser 9999")
	@Column(name = "NOMBER_EFF", length = 4)
	private Integer nomberEff;


}

