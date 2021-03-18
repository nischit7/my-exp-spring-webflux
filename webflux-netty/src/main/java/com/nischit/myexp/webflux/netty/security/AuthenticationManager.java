package com.nischit.myexp.webflux.netty.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

/**
 * Custom authentication manager.
 */
@Component("myAuthenticationManager")
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtHelper jwtHelper;

    private UserDetailsService userDetailsService;

    @Autowired
    public AuthenticationManager(
            @Qualifier("myUserDetailsService") final UserDetailsService userDetailsService,
            final JwtHelper jwtHelper) {
        this.userDetailsService = userDetailsService;
        this.jwtHelper = jwtHelper;
    }

    @Override
    public Mono<Authentication> authenticate(final Authentication authentication) {
        final String authToken = authentication.getCredentials().toString();

        try {
            if (!this.jwtHelper.validateJwtToken(authToken)) {
                return Mono.error(new BadCredentialsException("Authentication failed"));
            }
            final String username = this.jwtHelper.getUserNameFromJwtToken(authToken);
            final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            return Mono.just(new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername(),
                    null,
                    userDetails.getAuthorities()));
        } catch (final AuthenticationException ex) {
            return Mono.error(ex);
        }
    }
}
