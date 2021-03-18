package com.nischit.myexp.webflux.netty.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * Custom conext repository which retrieves bearer token.
 * It extends {@link WebSessionServerSecurityContextRepository} to use default abilities.
 */
@Component("mySecurityContextRepository")
public class SecurityContextRepository extends WebSessionServerSecurityContextRepository {

    private final AuthenticationManager authenticationManager;

    @Autowired
    public SecurityContextRepository(
            @Qualifier("myAuthenticationManager") final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Mono<Void> save(final ServerWebExchange exchange, final SecurityContext context) {
        return super.save(exchange, context);
    }

    @Override
    public Mono<SecurityContext> load(final ServerWebExchange exchange) {
        final ServerHttpRequest request = exchange.getRequest();
        final String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String authToken = authHeader.substring(7);
            final Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
            return this.authenticationManager
                .authenticate(auth)
                .onErrorMap(AuthenticationException.class, ex -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage()))
                .map(authentication -> new SecurityContextImpl(authentication));
        } else {
            return Mono.error(
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No bearer token sent in the request"));
        }
    }
}
