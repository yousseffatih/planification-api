package com.WALID.planification_api.playload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SingInRequest {
	@NotNull(message = "Username est obligatoire !")
	@NotEmpty(message = "Username est obligatoire!")
	private String username;

	@NotNull(message = "Password est obligatoire !")
	@NotEmpty(message = "Password est obligatoire!")
	private String password;
}
