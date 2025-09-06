package com.WALID.planification_api.controllers.parametrage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.WALID.planification_api.constants.GlobalConstant;
import com.WALID.planification_api.playload.MessageResponse;
import com.WALID.planification_api.playload.DTO.UsersDTO;
import com.WALID.planification_api.repositories.UserRepository;
import com.WALID.planification_api.services.Users.InUsersServices;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	@Autowired
	private InUsersServices usersServices;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("")
	private ResponseEntity<List<UsersDTO>> getlistUsers()
	{
		List<UsersDTO> userDTOs = usersServices.getUsers();
		return new ResponseEntity<>(userDTOs, HttpStatus.OK);
	}


	@GetMapping("/{id}")
	private ResponseEntity<UsersDTO> getlistUsersById(@PathVariable Long id)
	{
		UsersDTO userDTO = usersServices.getUsers(id);
		return new ResponseEntity<>(userDTO, HttpStatus.OK);
	}

	@PostMapping("")
	private ResponseEntity<?> addUser(@Valid @RequestBody UsersDTO userDTO)
	{
		boolean ifUsernameExists = userRepository.existsByUsernameAndStatut(userDTO.getUsername());
		if(ifUsernameExists)
		{
			return ResponseEntity.status(GlobalConstant.HTTPSTATUT_RESPONSE_ERORR).body(new MessageResponse("Username existe déjà !" , "warning"));
		}
		usersServices.addUsers(userDTO);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Utilisateur ajouté.","success"));
	}

	@GetMapping("/desactiveUser/{id}")
	private ResponseEntity<?> desactiverUser(@PathVariable Long id)
	{
		usersServices.desactiveUser(id);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Utilisateur désactivé.","success"));

	}

	@GetMapping("/activeUser/{id}")
	private ResponseEntity<?> activerUser(@PathVariable Long id)
	{
		usersServices.activeUser(id);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Utilisateur activé.","success"));

	}
	
	@GetMapping("/refrechPassword/{id}")
	private ResponseEntity<?> refrechPassword(@PathVariable Long id)
	{
		usersServices.refrechPassword(id);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Rerfreche password avec succee.","success"));

	}

	@GetMapping("/delete/{id}")
	private ResponseEntity<?> deleteUser(@PathVariable Long id)
	{
		usersServices.deleteUsersStatut(id);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Utilisateur supprimé.","success"));

	}
}
