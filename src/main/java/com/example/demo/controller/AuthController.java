package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import com.example.demo.infra.security.AuthUser;
import com.example.demo.infra.security.AuthUserService;
import com.example.demo.infra.security.LoginRequestDTO;
import com.example.demo.infra.security.TokenDTO;
import com.example.demo.infra.security.TokenService;

//@Tag(name = "Authentication", description = "Authentication and token handling")

@RestController
@RequestMapping("/login")
public class AuthController {

	private final TokenService tokenService;
	private final AuthenticationManager authenticationManager;
	private final AuthUserService authUserService;
	
	public AuthController(TokenService tokenService, AuthenticationManager authenticationManager, AuthUserService authUserService) {
		this.tokenService = tokenService;
		this.authenticationManager = authenticationManager;
		this.authUserService = authUserService;
	}

	// TODO: Swagger Doc
	// // io.swagger.v3.oas.annotations...
	// @Operation()
	// @ApiResponses({})
	
	@PostMapping
	public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginRequestDTO authData) {
		UsernamePasswordAuthenticationToken authToken =
				new UsernamePasswordAuthenticationToken(authData.email(), authData.password());
		Authentication auth = authenticationManager.authenticate(authToken);
		
		AuthUser user = (AuthUser) auth.getPrincipal();
		// Excepciones posibles para validateAccess:
		// org.springframework.security.authentication.DisabledException;
		// org.springframework.security.authentication.LockedException;
		authUserService.validateAccess(user);
		
		String tokenJWT = tokenService.createToken(user);
		return ResponseEntity.ok(new TokenDTO(tokenJWT));
	}
	
}
