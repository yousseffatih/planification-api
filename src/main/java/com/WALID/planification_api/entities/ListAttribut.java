package com.WALID.planification_api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "LIST_ATTRIBUT")
public class ListAttribut extends ClassEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(max = 50)
	@Column(name = "VALUE", length = 50)
	private String value;

	@Size(max = 50)
	@Column(name = "LIBELLE", length = 50)
	private String libelle;

	@Size(max = 50)
	@Column(name = "LIST_NAME_API", length = 50)
	private String listNameApi;

	@Size(max = 50)
	@Column(name = "LIST_Libelle", length = 50)
	private String listLibelle;
}
