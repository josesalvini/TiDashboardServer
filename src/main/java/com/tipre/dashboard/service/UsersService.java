package com.tipre.dashboard.service;

import java.util.List;
import java.util.Optional;

import com.tipre.dashboard.model.user.User;

public interface UsersService {
	
	public User save(User user);
	public List<User> findAll();
	public Optional<User> findById(Long id);
	public Optional<User> findByUsername(String username);
	public void deleteById(Long id);
	public boolean existsByUsername(String username);
	
	

}
