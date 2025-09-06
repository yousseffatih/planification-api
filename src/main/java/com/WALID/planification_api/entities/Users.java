package com.WALID.planification_api.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USERS")
public class Users extends ClassEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(max = 25)
	@Column(name="USERSNAME", length = 25)
	private String username;

	@Size(max = 50)
	@Column(name="EMAIL", length = 50)
	private String email;

	@Size(max = 25)
	@Column(name="NOM", length = 25)
	private String nom;

	@Size(max = 25)
	@Column(name="PRENOM", length = 25)
	private String prenom;

	@Size(max = 2)
	private String first;

	@Size(max = 100)
	@Column(name="PASSWORD", length = 100)
	private String password;

	//@ElementCollection
	//@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    //private List<UserRole> roles;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="ROLE_ID")
	private Roles role;
}
