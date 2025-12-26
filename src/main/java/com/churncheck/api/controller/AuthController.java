package com.churncheck.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.churncheck.api.infra.security.AuthUser;
import com.churncheck.api.infra.security.AuthUserService;
import com.churncheck.api.infra.security.LoginRequestDTO;
import com.churncheck.api.infra.security.TokenDTO;
import com.churncheck.api.infra.security.TokenService;

import jakarta.validation.Valid;

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
