package com.WALID.planification_api.controllers.parametrage;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.WALID.planification_api.constants.GlobalConstant;
import com.WALID.planification_api.playload.MessageResponse;
import com.WALID.planification_api.playload.DTO.UsersDTO;
import com.WALID.planification_api.repositories.UserRepository;
import com.WALID.planification_api.services.Security.JWTService;
import com.WALID.planification_api.services.Users.InUsersServices;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private InUsersServices usersServices;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JWTService jwtService;

	@GetMapping("")
	public ResponseEntity<List<UsersDTO>> getlistUsers(@RequestHeader("Authorization") String token) {
		if (token != null && token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		Long idToken = jwtService.extractUserId(token);
		System.out.println("idToken =======> " + idToken);
		List<UsersDTO> userDTOs = usersServices.getAllUsers(idToken);
		return new ResponseEntity<>(userDTOs, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UsersDTO> getlistUsersById(@PathVariable Long id) {
		UsersDTO userDTO = usersServices.getUserById(id);
		return new ResponseEntity<>(userDTO, HttpStatus.OK);
	}

	@PostMapping("")
	public ResponseEntity<?> addUser(@Valid @RequestBody UsersDTO userDTO) {
		boolean ifUsernameExists = userRepository.existsByUsernameAndStatut(userDTO.getUsername());
		if (ifUsernameExists) {
			return ResponseEntity.status(GlobalConstant.HTTPSTATUT_RESPONSE_ERORR)
					.body(new MessageResponse("Username existe déjà !", "warning"));
		}
		usersServices.addUsers(userDTO);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Utilisateur ajouté.", "success"));
	}

	@GetMapping("/desactiveUser/{id}")
	public ResponseEntity<?> desactiverUser(@PathVariable Long id) {
		usersServices.desactiveUser(id);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Utilisateur désactivé.", "success"));

	}

	@GetMapping("/activeUser/{id}")
	public ResponseEntity<?> activerUser(@PathVariable Long id) {
		usersServices.activeUser(id);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Utilisateur activé.", "success"));

	}

	@GetMapping("/refrechPassword/{id}")
	public ResponseEntity<?> refrechPassword(@PathVariable Long id) {
		usersServices.refrechPassword(id);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new MessageResponse("Rerfreche password avec succee.", "success"));

	}

	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id, @RequestParam Map<String, String> motif) {
		if (motif.get("motif") == null || motif.get("motif").isEmpty()) {
			return ResponseEntity.status(GlobalConstant.HTTPSTATUT_RESPONSE_ERORR)
					.body(new MessageResponse("Motif manquant.", "warning"));
		}
		usersServices.deleteUsersStatut(id, motif.get("motif"));
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Utilisateur supprimé.", "success"));

	}
}
