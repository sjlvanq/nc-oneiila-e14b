package com.churncheck.api.infra.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.churncheck.api.domain.user.User;

public class AuthUser implements UserDetails {
    private static final long serialVersionUID = 1L;
	private Long id;
    private String email;
    private String password;
    private List<GrantedAuthority> authorities;
    private boolean isActive;

    private final User user;
       
    public AuthUser(User user) {
    	this.user = user;
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPasswordHash();
        this.authorities = user.getRoles().stream()
            .map(r -> new SimpleGrantedAuthority(r.getName()))
            .collect(Collectors.toList());
        this.isActive = user.getActive();
    }

    
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public User getUser() {
		return user;
	}
    
    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return email; }
	@Override public Collection<? extends GrantedAuthority> getAuthorities() {return authorities;}

	public boolean isActive() {
		return isActive;
	}

	/*
	public boolean isAdmin() {
		return getAuthorities().stream().anyMatch(role -> ("ROLE_" + RoleConstants.ADMIN).equals(role.getAuthority()));
	}
	*/
}