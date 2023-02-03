package com.tipre.dashboard.controller.authentication;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tipre.dashboard.auth.AuthenticationError;
import com.tipre.dashboard.auth.AuthenticationRequest;
import com.tipre.dashboard.auth.AuthenticationResponse;
import com.tipre.dashboard.auth.RegisterResponse;
import com.tipre.dashboard.auth.service.JwtUtils;
import com.tipre.dashboard.auth.service.UserDetailsImpl;
import com.tipre.dashboard.model.user.ERole;
import com.tipre.dashboard.model.user.Role;
import com.tipre.dashboard.model.user.User;
import com.tipre.dashboard.repository.RoleRepository;
import com.tipre.dashboard.service.UsersService;

@RestController
@RequestMapping("/api/v1/auth")
//@CrossOrigin(origins = "http://localhost:59968, http://localhost:8080")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	private UsersService usersService;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	JwtUtils jwtUtils;

	@PostMapping(value = "/register")
	public ResponseEntity<?> register(@Validated @RequestBody User request) {

		try {
			if (usersService.existsByUsername(request.getUsername())) {
				return ResponseEntity.badRequest().body(AuthenticationError.builder()
						.code(HttpStatus.BAD_REQUEST.value()).message("El nombre de usuario ya existe!").build());
			}

			User user = User
					.builder()
					.firstname(request.getFirstname())
					.lastname(request.getLastname())
					.username(request.getUsername())
					.password(encoder.encode(request.getPassword()))
					.build();
			
			Set<Role> roles = new HashSet<>();
			
			if(request.getRoles() != null) {				
				roles = request.getRoles();
				LOGGER.info("ROLES {}", request.getRoles());
			}else {
				Role userRole = roleRepository.findByName(ERole.USER)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(userRole);
			}
			
			/*
			if (roles.isEmpty()) {

			} else {
				
				for (Role role : roles) {
					switch (role.getName().name().toUpperCase()) {
					case "ADMIN":
						Role adminRole = roleRepository.findByName(ERole.ADMIN)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(adminRole);

						break;
					default:
						Role userRole = roleRepository.findByName(ERole.USER)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(userRole);
					}
				}
				roles.forEach(role -> {

				});
			}*/

			user.setRoles(roles);
			usersService.save(user);

			return ResponseEntity.ok().body(RegisterResponse.builder().user(user).build());
		} catch (RuntimeException e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.internalServerError().body(AuthenticationError.builder()
					.code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(e.getMessage()).build());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.internalServerError().body(AuthenticationError.builder()
					.code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(e.getMessage()).build());
		}

	}

	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(@Validated @RequestBody AuthenticationRequest request) {
		Optional<User> user = usersService.findByUsername(request.getUsername());

		if (user.isPresent()) {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

			String token = jwtUtils.generateTokenFromUsername(userDetails.getUsername());

			// ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

			// List<String> roles = userDetails.getAuthorities().stream().map(item ->
			// item.getAuthority())
			// .collect(Collectors.toList());

			return ResponseEntity.ok()
					// .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
					.body(AuthenticationResponse.builder().user(user.get()).token(token).build());
		} else {
			return ResponseEntity.badRequest()
					.body(AuthenticationError
							.builder()
							.code(HttpStatus.BAD_REQUEST.value())
							.message("El usuario ".concat(request.getUsername()).concat(" no existe"))
							.build());
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logoutUser(HttpServletRequest request) throws ServletException {
		request.logout();
		return ResponseEntity.ok().body("Desconectado!");
	}


	

}
