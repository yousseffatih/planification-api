package com.WALID.planification_api.playload;

import java.util.List;


import lombok.Data;

@Data
public class JwtAuthentication {

	private String token;
	private String refrechToken;
	private String username;
	private String email;
	private String nom;
	private String prenom;
	private Long idUser;
	private String first;
	//private List<String> roles;
	private String role;

}
