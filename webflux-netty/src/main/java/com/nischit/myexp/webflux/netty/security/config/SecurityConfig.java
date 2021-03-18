package com.nischit.myexp.webflux.netty.security.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

import com.nischit.myexp.webflux.netty.security.AuthenticationManager;
import com.nischit.myexp.webflux.netty.security.SecurityContextRepository;

/**
 * Spring security config.
 */
@EnableWebFluxSecurity
@ComponentScan(basePackages = "com.nischit.myexp.webflux.netty.security")
public class SecurityConfig {

    /**
     * Spring security filter chain configuration.
     *
     * @param http An instance of {@link ServerHttpSecurity}.
     * @param authenticationManager An instance of {@link AuthenticationManager}.
     * @param securityContextRepository An instance of {@link SecurityContextRepository}.
     * @return Configured {@link SecurityWebFilterChain}.
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            final ServerHttpSecurity http,
            @Qualifier("myAuthenticationManager") final AuthenticationManager authenticationManager,
            @Qualifier("mySecurityContextRepository") final SecurityContextRepository securityContextRepository) {
        return http
            .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/api/setup/**"))
            .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec.authenticationEntryPoint(
                    new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)))
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .authenticationManager(authenticationManager)
            .securityContextRepository(securityContextRepository)
            .authorizeExchange()
            .anyExchange()
            .authenticated()
            .and().build();
    }

    /**
     * Spring security filter chain configuration.
     *
     * @param http An instance of {@link ServerHttpSecurity}.
     * @return Configured {@link SecurityWebFilterChain}.
     */
    @Bean
    public SecurityWebFilterChain securityActuator(
            final ServerHttpSecurity http) {
        return http
            .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/manage/**"))
            .authorizeExchange()
            .anyExchange()
            .permitAll()
            .and().build();
    }
}
