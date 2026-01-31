package com.WALID.planification_api.services.Users;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.WALID.planification_api.constants.GlobalConstant;
import com.WALID.planification_api.entities.Roles;
import com.WALID.planification_api.entities.Users;
import com.WALID.planification_api.playload.ResourceNotFoundException;
import com.WALID.planification_api.playload.DTO.UsersDTO;
import com.WALID.planification_api.repositories.UserRepository;
import com.WALID.planification_api.repositories.Parametrage.RolesRepository;

@Service
public class UsersServicesImp implements InUsersServices {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RolesRepository rolesRepository;

	@Override
	public List<UsersDTO> getAllUsers(Long id) {
		List<Users> list = userRepository.findUsersByStatut(id);
		List<UsersDTO> listsDtos = list.stream().map((l) -> mapToDTO(l)).collect(Collectors.toList());
		return listsDtos;
	}

	@Override
	public UsersDTO getUserById(Long id) {
		Users user = userRepository.findByIdAndStatutList(id)
				.orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", id));
		return mapToDTO(user);
	}

	@Override
	public UsersDTO addUsers(UsersDTO userDTO) {
		Roles role = rolesRepository.findByIdStatut(userDTO.getIdRole())
				.orElseThrow(() -> new ResourceNotFoundException("Role", "id", userDTO.getIdRole()));

		Users addUser = new Users();

		addUser.setStatut(GlobalConstant.STATUT_ACTIF);
		addUser.setDateCreation(new Date());

		addUser.setUsername(userDTO.getUsername());
		addUser.setEmail(userDTO.getEmail());
		addUser.setPassword(passwordEncoder.encode(userDTO.getUsername()));
		addUser.setFirst("1");
		addUser.setNom(userDTO.getNom());
		addUser.setPrenom(userDTO.getPrenom());
		addUser.setRole(role);

		return mapToDTO(userRepository.save(addUser));
	}

	@Override
	public UsersDTO deleteUsersStatut(Long id, String motif) {
		Users user = userRepository.findByIdAndStatutList(id)
				.orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", id));
		user.setStatut(GlobalConstant.STATUT_DELETE);
		user.setDateDesactivation(new Date());
		user.setMotif(motif);
		return mapToDTO(userRepository.save(user));
	}

	@Override
	public UsersDTO desactiveUser(Long id) {
		Users user = userRepository.findByIdAndStatutList(id)
				.orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", id));
		user.setStatut(GlobalConstant.STATUT_INACTIF);
		user.setDateModification(new Date());
		return mapToDTO(userRepository.save(user));
	}

	@Override
	public UsersDTO activeUser(Long id) {
		Users user = userRepository.findByIdAndStatutList(id)
				.orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", id));
		user.setStatut(GlobalConstant.STATUT_ACTIF);
		user.setDateModification(new Date());
		return mapToDTO(userRepository.save(user));
	}

	@Override
	public void refrechPassword(Long id) {
		Users user = userRepository.findByIdAndStatutList(id)
				.orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", id));
		user.setStatut(GlobalConstant.STATUT_ACTIF);
		user.setDateModification(new Date());
		user.setPassword(passwordEncoder.encode(user.getUsername()));
		user.setFirst("1");
		userRepository.save(user);
	}

	private UsersDTO mapToDTO(Users x) {
		UsersDTO dto = new UsersDTO();
		dto.setId(x.getId());
		dto.setUsername(x.getUsername());
		dto.setEmail(x.getEmail());
		dto.setDateCreation(x.getDateCreation());
		dto.setDateModification(x.getDateModification());
		dto.setDateDesactivation(x.getDateDesactivation());
		dto.setStatut(x.getStatut());
		dto.setNom(x.getNom());
		dto.setPrenom(x.getPrenom());
		dto.setIdRole(x.getRole().getId());
		dto.setLibelleRole(x.getRole().getNom());
		return dto;
	}

}
