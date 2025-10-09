package com.WALID.planification_api.services.Security;

import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.WALID.planification_api.entities.Users;
import com.WALID.planification_api.exceptions.CustomException;
import com.WALID.planification_api.repositories.UserRepository;
import com.WALID.planification_api.playload.ChangePassword;
import com.WALID.planification_api.playload.JwtAuthentication;
import com.WALID.planification_api.playload.RefreshTokenRequest;
import com.WALID.planification_api.playload.SingInRequest;


@Service
public class AuthentificationServices {


	private  UserRepository userRepository;
	private  PasswordEncoder passwordEncoder;
	private AuthenticationManager authenticationManager;
	private JWTService jwtService;




	public AuthentificationServices (UserRepository userRepository, PasswordEncoder passwordEncoder,JWTService jwtService, AuthenticationManager authenticationManager) {
		this.userRepository= userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}


	public JwtAuthentication singIn(SingInRequest singInRequest)
	{
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(singInRequest.getUsername(), singInRequest.getPassword()));

		Users user = userRepository.findByUsernameStatut(singInRequest.getUsername())
				.orElseThrow(() -> new IllegalArgumentException("Invalid Username or password"));

		if(user.getFirst().equals("1"))
		{
			return null;
		}

		String jwt = jwtService.generateToken(user.getUsername(), user.getId());
		String refreshJwt = jwtService.generateRefreshToken(new HashMap<>(),user.getUsername());

		JwtAuthentication jwtAythentication =new JwtAuthentication();

		jwtAythentication.setIdUser(user.getId());
		//jwtAythentication.setRoles(user.getRoles().stream().map(role -> role.getRole().getNom()).collect(Collectors.toList()));
		jwtAythentication.setRole(user.getRole().getNom());
		jwtAythentication.setFirst(user.getFirst());
		jwtAythentication.setEmail(user.getEmail());
		jwtAythentication.setNom(user.getNom());
		jwtAythentication.setUsername(user.getUsername());
		jwtAythentication.setPrenom(user.getPrenom());
		jwtAythentication.setToken(jwt);

		jwtAythentication.setRefrechToken(refreshJwt);

		return jwtAythentication;
	}

	public void changePassword(ChangePassword changePassword) {
		Users user = userRepository.findByUsernameStatut(changePassword.getUsername())
				.orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

		if(!passwordEncoder.matches(changePassword.getOldPassword(), user.getPassword()))
		{
			 throw new CustomException("Votre mot de passe est incorrect !");
		}
		else	
		{
			user.setDateModification(new Date());
			user.setFirst("0");
			user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));

			userRepository.save(user);
		}
	}


	public JwtAuthentication refreshToken(RefreshTokenRequest refreshTokenRequest)
	{
		String token = refreshTokenRequest.getToken();
		String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());
		Users user = getUser(userEmail);

		if(user == null )
		{
			throw new IllegalArgumentException("Invalid user");
		}

		UserPrincipal userPrincipal = new UserPrincipal(user);

		if(jwtService.validateToken(token,userPrincipal))
		{
			String jwt = jwtService.generateToken(userPrincipal.getUsername(), userPrincipal.getUser().getId());

			JwtAuthentication jwtAythentication =new JwtAuthentication();

			//jwtAythentication.setUser(user);
			jwtAythentication.setToken(jwt);
			jwtAythentication.setRefrechToken(refreshTokenRequest.getToken());

			return jwtAythentication;
		}
		return null;
	}

	public Users getUser(String username)
	{
		Users user = userRepository.findByUsernameStatut(username).orElseThrow(()-> new CustomException("User Not found"));
		return user;
	}
}