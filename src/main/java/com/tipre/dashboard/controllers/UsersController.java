package com.tipre.dashboard.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tipre.dashboard.dto.ResponseMessage;
import com.tipre.dashboard.dto.UserUpdate;
import com.tipre.dashboard.dto.UsersResponse;
import com.tipre.dashboard.model.user.ERole;
import com.tipre.dashboard.model.user.Role;
import com.tipre.dashboard.model.user.User;
import com.tipre.dashboard.repository.RoleRepository;
import com.tipre.dashboard.service.UsersService;


@RestController
@RequestMapping("/api/v1/users")
//@CrossOrigin(origins = "*", maxAge = 3600)
@CrossOrigin(origins = "*",
exposedHeaders = {"Access-Control-Allow-Origin","Access-Control-Allow-Credentials"},
allowedHeaders = {"Authorization", "Origin"}, 
maxAge = 3600)
public class UsersController {

	//private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);
	
	@Autowired
	private UsersService usersService;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	PasswordEncoder encoder;
	
	@GetMapping
	public ResponseEntity<?> getUsers() {
		try {
			List<User> users = usersService.findAll();

			if (users.isEmpty()) {
				users = new ArrayList<>(0);
				return new ResponseEntity<>(
						UsersResponse
						.builder()
						.count(users.size())
						.users(users)
						.build(), 
						HttpStatus.NO_CONTENT);
			}
			
			return new ResponseEntity<>(  
					UsersResponse
					.builder()
					.count(users.size())
					.users(users)
					.build(), 
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(
					ResponseMessage
					.builder()
					.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
					.message(e.getMessage())
					.build(), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(path = "{id}")
	public ResponseEntity<?> getUser(@Valid @PathVariable("id") Long id) {
		Optional<User> user = usersService.findById(id);

		if (user.isPresent()) {
			return new ResponseEntity<>(user.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					ResponseMessage
					.builder()
					.statusCode(HttpStatus.NOT_FOUND.value())
					.message("Usuario no encontrado.")
					.build(), 
					HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
		try {
			User _user = User
							.builder()
							.firstname(user.getFirstname())
							.lastname(user.getLastname())
							.username(user.getUsername())
							.password(encoder.encode(user.getPassword()))
							.build();
			
			Set<Role> roles = new HashSet<>();
			
			if(user.getRoles() != null) {				
				roles = user.getRoles();
			}else {
				Role role = roleRepository.findByName(ERole.USER)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(role);
			}

			user.setRoles(roles);

			usersService.save(_user);

			return new ResponseEntity<>(_user, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(
					ResponseMessage
					.builder()
					.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
					.message(e.getMessage())
					.build(), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(path = "{id}")
	public ResponseEntity<?> deleteUser(@Valid @PathVariable("id") Long id) {
		try {
			usersService.deleteById(id);
			return new ResponseEntity<>(
					ResponseMessage
					.builder()
					.statusCode(HttpStatus.OK.value())
					.message("Usuario eliminado correctamente.")
					.build(), 
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(
					ResponseMessage
					.builder()
					.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
					.message(e.getMessage())
					.build(), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(path = "{id}")
	public ResponseEntity<?> updateUser(@Valid @PathVariable("id") Long id, @Valid @RequestBody UserUpdate user) {
		Optional<User> userData = usersService.findById(id);

		if (userData.isPresent()) {
			User _user = userData.get();
			
			_user.setFirstname(user.getFirstname());
			_user.setLastname(user.getLastname());
			_user.setUsername(user.getUsername());
			
			return new ResponseEntity<>(usersService.save(_user), 
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					ResponseMessage
					.builder()
					.statusCode(HttpStatus.NOT_FOUND.value())
					.message("Usuario no encontrado.")
					.build(), 
					HttpStatus.NOT_FOUND);
		}
	}
}
