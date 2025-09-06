package com.WALID.planification_api.services.Security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.WALID.planification_api.entities.Roles;
import com.WALID.planification_api.entities.UserRole;
import com.WALID.planification_api.entities.Users;
import com.WALID.planification_api.repositories.UserRepository;
import com.WALID.planification_api.repositories.UserRoleRepository;
import com.WALID.planification_api.repositories.Parametrage.RolesRepository;





@Service
public class UserService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;

	//@Autowired
	//private UserRoleRepository userRoleRepository;
	
	@Autowired
	private RolesRepository rolesRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Users user = userRepository.findByUsernameStatut(username)
				.orElseThrow(()-> new UsernameNotFoundException("Utilisateur non trouvé avec ce nom d'utilisateur "+ username));

		if(user == null)
		{
			throw new UsernameNotFoundException("Utilisateur non trouvé.");
		}

		//List<UserRole> userRoles = userRoleRepository.getDroits(user.getId());
		
		Roles roles = rolesRepository.findById(user.getRole().getId()).orElseThrow(null);

		user.setRole(roles);

		return new UserPrincipal(user);
	}

}
