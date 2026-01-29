package com.WALID.planification_api.services.Users;

import java.util.List;

import com.WALID.planification_api.playload.DTO.UsersDTO;

public interface InUsersServices {

	public List<UsersDTO> getAllUsers(Long idToken);

	public UsersDTO getUserById(Long id);

	public UsersDTO addUsers(UsersDTO userDTO);

	public UsersDTO desactiveUser(Long id);

	public UsersDTO activeUser(Long id);

	public UsersDTO deleteUsersStatut(Long id);

	public void refrechPassword(Long id);
}
