package com.churncheck.api.infra.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.churncheck.api.domain.user.UserRepository;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.length() > "BEARER ".length() &&
                authHeader.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
            String token = authHeader.split(" ")[1].trim();
            String userEmail = tokenService.getSubject(token);

            if (userEmail != null) {
                // Token is valid
                AuthUser authUser = new AuthUser(userRepository.findByEmailWithRoles(userEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found")));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authUser,
                        null, authUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
