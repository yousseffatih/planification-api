package com.WALID.planification_api.playload.DTO;



import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableSalleBody {

	@NotNull(message = "Date Planification est obligatoire!")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private String datePlanification;

	@NotNull(message = "Time start est obligatoire!")
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime timeStarTime;

	@NotNull(message = "Time End est obligatoire!")
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime timeEnDateTime;
}
