package com.churncheck.api.domain.user;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {

    private User user;
    private Role role1;
    private Role role2;

    @BeforeEach
    void setUp() {
        user = new User();
        role1 = new Role();
        role1.setId(1L);
        role1.setName("ADMIN");
        
        role2 = new Role();
        role2.setId(2L);
        role2.setName("USER");
    }

    @Test
    void shouldCreateUserWithDefaultValues() {
        // When
        User newUser = new User();
        
        // Then
        assertNotNull(newUser);
        assertNotNull(newUser.getRoles());
        assertTrue(newUser.getRoles().isEmpty());
    }

    @Test
    void shouldSetAndGetUserProperties() {
        // Given
        Long expectedId = 1L;
        String expectedEmail = "test@example.com";
        String expectedPasswordHash = "hashedPassword";
        Boolean expectedActive = true;
        String expectedName = "Test User";
        
        // When
        setField(user, "id", expectedId);
        setField(user, "email", expectedEmail);
        setField(user, "passwordHash", expectedPasswordHash);
        setField(user, "active", expectedActive);
        setField(user, "name", expectedName);
        
        // Then
        assertEquals(expectedId, user.getId());
        assertEquals(expectedEmail, user.getEmail());
        assertEquals(expectedPasswordHash, user.getPasswordHash());
        assertEquals(expectedActive, user.getActive());
        assertEquals(expectedName, user.getName());
    }

    @Test
    void shouldManageRoles() {
        // Given
        Set<Role> roles = new HashSet<>();
        roles.add(role1);
        roles.add(role2);
        
        // When
        setField(user, "roles", roles);
        
        // Then
        assertEquals(2, user.getRoles().size());
        assertTrue(user.getRoles().contains(role1));
        assertTrue(user.getRoles().contains(role2));
    }

    @Test
    void shouldAddSingleRole() {
        // When
        user.getRoles().add(role1);
        
        // Then
        assertEquals(1, user.getRoles().size());
        assertEquals(role1, user.getRoles().iterator().next());
    }

    @Test
    void shouldRemoveRole() {
        // Given
        user.getRoles().add(role1);
        user.getRoles().add(role2);
        assertEquals(2, user.getRoles().size());
        
        // When
        user.getRoles().remove(role1);
        
        // Then
        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().contains(role2));
        assertFalse(user.getRoles().contains(role1));
    }

    @Test
    void shouldHandleEmptyRoles() {
        // Given
        user.getRoles().add(role1);
        
        // When
        user.getRoles().clear();
        
        // Then
        assertTrue(user.getRoles().isEmpty());
        assertEquals(0, user.getRoles().size());
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
