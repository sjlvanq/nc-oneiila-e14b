package com.churncheck.api.domain.user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
    }

    @Test
    void shouldCreateRoleWithDefaultValues() {
        // When
        Role newRole = new Role();
        
        // Then
        assertNotNull(newRole);
        assertNull(newRole.getId());
        assertNull(newRole.getName());
    }

    @Test
    void shouldSetAndGetRoleProperties() {
        // Given
        Long expectedId = 1L;
        String expectedName = "ADMIN";
        
        // When
        role.setId(expectedId);
        role.setName(expectedName);
        
        // Then
        assertEquals(expectedId, role.getId());
        assertEquals(expectedName, role.getName());
    }

    @Test
    void shouldUpdateRoleProperties() {
        // Given
        role.setId(1L);
        role.setName("USER");
        
        // When
        role.setId(2L);
        role.setName("ADMIN");
        
        // Then
        assertEquals(2L, role.getId());
        assertEquals("ADMIN", role.getName());
    }

    @Test
    void shouldHandleNullName() {
        // When
        role.setName(null);
        
        // Then
        assertNull(role.getName());
    }

    @Test
    void shouldHandleEmptyName() {
        // When
        role.setName("");
        
        // Then
        assertEquals("", role.getName());
    }

    @Test
    void shouldHandleLongName() {
        // Given
        String longName = "VERY_LONG_ROLE_NAME_WITH_MANY_CHARACTERS_AND_NUMBERS_123456789";
        
        // When
        role.setName(longName);
        
        // Then
        assertEquals(longName, role.getName());
    }
}
