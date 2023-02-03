package com.tipre.dashboard.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tipre.dashboard.model.user.User;
import com.tipre.dashboard.repository.UserRepository;

@Service
public class UsersServiceImpl implements UsersService {

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public List<User> findAll() {
		
		List<User> users = userRepository.findAll();
		
		return users;
	}

	@Override
	@Transactional
	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}

	@Override
	@Transactional
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	@Transactional
	public User save(User user) {	
		return userRepository.save(user);
	}

	@Override
	@Transactional
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}
	
	
}
