package com.WALID.planification_api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.WALID.planification_api.entities.Users;
import com.WALID.planification_api.exceptions.CustomException;
import com.WALID.planification_api.playload.ChangePassword;
import com.WALID.planification_api.playload.JwtAuthentication;
import com.WALID.planification_api.playload.MessageResponse;
import com.WALID.planification_api.playload.RefreshTokenRequest;
import com.WALID.planification_api.playload.SingInRequest;
import com.WALID.planification_api.services.Security.AuthentificationServices;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	@Autowired
	private AuthentificationServices authentificationServices;

	@Autowired
    public void Authentication(AuthentificationServices authentificationServices) {
        this.authentificationServices = authentificationServices;
    }

	@PostMapping("/signIn")
	public ResponseEntity<?> singIn(@Valid @RequestBody SingInRequest singInRequest)
	{
//		try {
//			return ResponseEntity.ok(authentificationServices.singIn(singInRequest));
//		} catch (CustomException e)
//		{
//			//return new ResponseEntity<>(new MessageResponse(e.getMessage(), "warning"), HttpStatus.BAD_REQUEST);
//			return  ResponseEntity.status(210).body(new MessageResponse(e.getMessage(),"warning"));
//		}
		try {
			JwtAuthentication jwtAythentication = authentificationServices.singIn(singInRequest);
			if(jwtAythentication == null)
			{
				return  ResponseEntity.status(451).body(new MessageResponse("Mot de passe réinitialisé !!","warning"));
			} else {
				return ResponseEntity.ok(jwtAythentication);
			}
		} catch (CustomException e)
		{
			return new ResponseEntity<>(new MessageResponse(e.getMessage(), "warning"), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/changePassword")
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePassword changePassword)
	{
		try {
			authentificationServices.changePassword(changePassword);
			 return new ResponseEntity<>(new MessageResponse("Mot de passe changé avec succès.","success") , HttpStatus.OK);
		} catch (CustomException e) {
			return new ResponseEntity<>(new MessageResponse(e.getMessage(),"warning") , HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/refresh")
	public ResponseEntity<JwtAuthentication> singIn(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest)
	{
		return ResponseEntity.ok(authentificationServices.refreshToken(refreshTokenRequest));
	}

	@PostMapping("/user")
	public ResponseEntity<Users> getUser(@Valid @RequestBody SingInRequest singInRequest)
	{
		Users user = authentificationServices.getUser(singInRequest.getUsername());
		return new ResponseEntity<>(user,HttpStatus.OK);
	}
}
