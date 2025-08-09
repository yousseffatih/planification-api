package com.WALID.planification_api.playload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RefreshTokenRequest {

	@NotNull(message = "Token est obligatoire!")
	@NotEmpty(message = "Token est obligatoire!")
	private String token;
}
