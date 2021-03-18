package com.nischit.myexp.webflux.netty.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Represents each authenticated user.
 */
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = -5609590488418090712L;

    private String username;

    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(
            final String username,
            final String password,
            final Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Builds {@link UserDetailsImpl} using {@link User}.
     *
     * @param user An instance of {@link User}.
     * @return An instance of {@link UserDetailsImpl}.
     */
    public static UserDetailsImpl build(final User user) {
        return new UserDetailsImpl(
            user.getUsername(),
            user.getPassword(),
            user.getAuthorities());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
