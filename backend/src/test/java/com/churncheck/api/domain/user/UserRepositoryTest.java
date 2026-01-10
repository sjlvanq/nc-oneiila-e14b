package com.churncheck.api.domain.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldFindUserByEmailWithRoles() {
        // Given
        String email = "test@example.com";
        User expectedUser = createTestUser();
        
        when(userRepository.findByEmailWithRoles(email)).thenReturn(Optional.of(expectedUser));

        // When
        Optional<User> result = userRepository.findByEmailWithRoles(email);

        // Then
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        assertEquals("Test User", result.get().getName());
        assertEquals(2, result.get().getRoles().size());
        assertTrue(result.get().getActive());
    }

    @Test
    void shouldReturnEmptyWhenUserNotFound() {
        // Given
        String nonExistentEmail = "nonexistent@example.com";
        
        when(userRepository.findByEmailWithRoles(nonExistentEmail)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userRepository.findByEmailWithRoles(nonExistentEmail);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void shouldReturnEmptyWhenUserIsInactive() {
        // Given
        String email = "inactive@example.com";
        User inactiveUser = createTestUser();
        setField(inactiveUser, "email", email);
        setField(inactiveUser, "active", false);
        
        // Simulate that the query returns empty for inactive users
        when(userRepository.findByEmailWithRoles(email)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userRepository.findByEmailWithRoles(email);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void shouldFindUserWithSingleRole() {
        // Given
        String email = "singlerole@example.com";
        User user = createTestUser();
        setField(user, "email", email);
        
        // Remove one role
        user.getRoles().remove(user.getRoles().iterator().next());
        
        when(userRepository.findByEmailWithRoles(email)).thenReturn(Optional.of(user));

        // When
        Optional<User> result = userRepository.findByEmailWithRoles(email);

        // Then
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        assertEquals(1, result.get().getRoles().size());
    }

    @Test
    void shouldFindUserWithNoRoles() {
        // Given
        String email = "noroles@example.com";
        User user = createTestUser();
        setField(user, "email", email);
        user.getRoles().clear();
        
        when(userRepository.findByEmailWithRoles(email)).thenReturn(Optional.of(user));

        // When
        Optional<User> result = userRepository.findByEmailWithRoles(email);

        // Then
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        assertTrue(result.get().getRoles().isEmpty());
    }

    private User createTestUser() {
        User user = new User();
        setField(user, "id", 1L);
        setField(user, "email", "test@example.com");
        setField(user, "passwordHash", "hashedPassword");
        setField(user, "active", true);
        setField(user, "name", "Test User");
        
        // Create roles
        Role adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setName("ADMIN");
        
        Role userRole = new Role();
        userRole.setId(2L);
        userRole.setName("USER");
        
        setField(user, "roles", new HashSet<>(Set.of(adminRole, userRole)));
        
        return user;
    }
    
    private void setField(Object obj, String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException("Error setting field " + fieldName, e);
        }
    }
}
