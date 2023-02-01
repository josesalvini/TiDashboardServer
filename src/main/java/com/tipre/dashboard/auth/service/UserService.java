package com.tipre.dashboard.auth.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tipre.dashboard.model.user.Role;
import com.tipre.dashboard.model.user.User;
import com.tipre.dashboard.repository.RoleRepository;
import com.tipre.dashboard.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
  
  @Autowired
  private UserRepository userRepository;
  
  @Autowired
  private RoleRepository roleRepository;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuario - No existe el username: " + username));

    List<GrantedAuthority> grantedAuthority = new ArrayList<>();
    List<Role> roles = roleRepository.findAll();
    
    roles.forEach(role -> {
    	grantedAuthority.add(new SimpleGrantedAuthority(role.getName().name()));
    });
    
    return UserDetailsImpl
    		.builder()
    		.user(user)
    		.authorities(grantedAuthority)
    		.build();
  }
}