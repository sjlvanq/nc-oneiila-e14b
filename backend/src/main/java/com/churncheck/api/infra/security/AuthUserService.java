package com.churncheck.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.churncheck.api.domain.user.UserRepository;

@Service
public class AuthUserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new AuthUser(
        		userRepository.findByEmailWithRoles(email)
        			.orElseThrow(()->new BadCredentialsException("User email not found in system")));
    }

	public void validateAccess(AuthUser user) {
        if (!user.isActive()) {
            throw new DisabledException("Account for " + user.getUsername() + " is disabled.");
        }

        // Validaciones de lógica de negocio adicionales:
        // if (!user.getUsuario().isPerfilCompleto()) {
        //     throw new InsufficientAuthenticationException("Debe completar su perfil antes de acceder.");
        // }
	}
}