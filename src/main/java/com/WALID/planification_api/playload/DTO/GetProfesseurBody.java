package com.WALID.planification_api.playload.DTO;

import lombok.Data;

@Data
public class GetProfesseurBody {
	private Long idSalle;
	private Long idModule;
	private Long idType;
	private String dateDebut;
	private String dateFin;
	private Long idClasse;
	private Long idProf;
	private Long idVille;
	private Long idCumpus;
}
