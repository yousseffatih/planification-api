package com.WALID.planification_api.playload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePassword {

	@NotNull(message = "Username est obligatoire !")
	@NotEmpty(message = "Username est obligatoire!")
	private String username;

	@NotNull(message = "Ancien mot de passe est obligatoire !")
	@NotEmpty(message = "Ancien mot de passe est obligatoire!")
	private String oldPassword;

	@NotNull(message = "Nouveau mot de passe est obligatoire !")
	@NotEmpty(message = "Nouveau mot de passe est obligatoire!")
	private String newPassword;

}
