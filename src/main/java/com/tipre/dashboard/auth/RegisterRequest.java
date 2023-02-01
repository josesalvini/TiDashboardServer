package com.tipre.dashboard.auth;

import java.util.HashSet;
import java.util.Set;

import com.tipre.dashboard.model.user.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

	private String firstname;
	private String lastname;
	private String username;
	private String password;
	//private Set<Role> role;
	@Builder.Default
	private Set<Role> roles = new HashSet<>();
	
	
}
