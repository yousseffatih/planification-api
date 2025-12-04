package com.WALID.planification_api.services.Security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.WALID.planification_api.entities.Users;



public class UserPrincipal implements UserDetails{


	private Users user;

	public UserPrincipal(Users user) {
		this.user = user;
	}

	public Users getUser()
	{
		return user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		if(user.getRole()== null)
		{
			return null;
		}
		System.out.println("Role : "+user.getRole().getNom());
		return List.of(new SimpleGrantedAuthority("ROLE_"+user.getRole().getNom()));
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
