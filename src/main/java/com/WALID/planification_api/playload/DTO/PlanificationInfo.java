package com.WALID.planification_api.playload.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanificationInfo {
	
	private Integer totalReservation;
	private Integer reservationAnnuler;
	private Integer salleDesiponible;
	private Integer salleIndesponible;
	private Integer utilisateurActif;
}
