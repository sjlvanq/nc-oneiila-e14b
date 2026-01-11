package com.churncheck.api.infra.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;

import com.churncheck.api.domain.user.Role;
import com.churncheck.api.domain.user.User;
import com.churncheck.api.domain.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthUserService authUserService;

    private User activeUser;
    private User inactiveUser;
    private Set<Role> roles;

    @BeforeEach
    void setUp() {
        roles = new HashSet<>();
        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        roles.add(userRole);
    }

    @Test
    void shouldLoadUserByUsernameSuccessfully() {
        // Given
        String email = "user@example.com";
        activeUser = mock(User.class);
        when(activeUser.getId()).thenReturn(1L);
        when(activeUser.getEmail()).thenReturn("user@example.com");
        when(activeUser.getPasswordHash()).thenReturn("hashedPassword");
        when(activeUser.getActive()).thenReturn(true);
        when(activeUser.getRoles()).thenReturn(roles);
        
        when(userRepository.findByEmailWithRoles(email)).thenReturn(Optional.of(activeUser));

        // When
        UserDetails result = authUserService.loadUserByUsername(email);

        // Then
        assertNotNull(result);
        assertEquals(email, result.getUsername());
        assertEquals("hashedPassword", result.getPassword());
        assertTrue(result.isEnabled());
        assertEquals(1, result.getAuthorities().size());
        assertTrue(result.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
        
        verify(userRepository).findByEmailWithRoles(email);
    }

    @Test
    void shouldThrowBadCredentialsExceptionWhenUserNotFound() {
        // Given
        String email = "nonexistent@example.com";
        when(userRepository.findByEmailWithRoles(email)).thenReturn(Optional.empty());

        // When & Then
        BadCredentialsException exception = assertThrows(
            BadCredentialsException.class,
            () -> authUserService.loadUserByUsername(email)
        );

        assertEquals("User email not found in system", exception.getMessage());
        verify(userRepository).findByEmailWithRoles(email);
    }

    @Test
    void shouldValidateAccessForActiveUser() {
        // Given
        activeUser = mock(User.class);
        when(activeUser.getId()).thenReturn(1L);
        when(activeUser.getEmail()).thenReturn("user@example.com");
        when(activeUser.getPasswordHash()).thenReturn("hashedPassword");
        when(activeUser.getActive()).thenReturn(true);
        when(activeUser.getRoles()).thenReturn(roles);
        
        AuthUser authUser = new AuthUser(activeUser);

        // When & Then
        assertDoesNotThrow(() -> authUserService.validateAccess(authUser));
    }

    @Test
    void shouldThrowDisabledExceptionWhenUserInactive() {
        // Given
        inactiveUser = mock(User.class);
        when(inactiveUser.getId()).thenReturn(2L);
        when(inactiveUser.getEmail()).thenReturn("inactive@example.com");
        when(inactiveUser.getPasswordHash()).thenReturn("hashedPassword");
        when(inactiveUser.getActive()).thenReturn(false);
        when(inactiveUser.getRoles()).thenReturn(roles);
        
        AuthUser authUser = new AuthUser(inactiveUser);

        // When & Then
        DisabledException exception = assertThrows(
            DisabledException.class,
            () -> authUserService.validateAccess(authUser)
        );

        assertEquals("Account for inactive@example.com is disabled.", exception.getMessage());
    }

    @Test
    void shouldLoadUserByUsernameWithMultipleRoles() {
        // Given
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        Set<Role> multipleRoles = new HashSet<>(roles);
        multipleRoles.add(adminRole);

        User userWithMultipleRoles = mock(User.class);
        when(userWithMultipleRoles.getId()).thenReturn(3L);
        when(userWithMultipleRoles.getEmail()).thenReturn("admin@example.com");
        when(userWithMultipleRoles.getPasswordHash()).thenReturn("adminPassword");
        when(userWithMultipleRoles.getActive()).thenReturn(true);
        when(userWithMultipleRoles.getRoles()).thenReturn(multipleRoles);

        when(userRepository.findByEmailWithRoles("admin@example.com"))
            .thenReturn(Optional.of(userWithMultipleRoles));

        // When
        UserDetails result = authUserService.loadUserByUsername("admin@example.com");

        // Then
        assertNotNull(result);
        assertEquals(2, result.getAuthorities().size());
        assertTrue(result.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
        assertTrue(result.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        
        verify(userRepository).findByEmailWithRoles("admin@example.com");
    }
}
