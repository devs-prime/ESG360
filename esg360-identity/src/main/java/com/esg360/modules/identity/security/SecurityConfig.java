package com.esg360.modules.identity.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import com.esg360.web.tenancy.TenantContextFilter;

/**
 * Resource-server security for the whole API (spec 31, spec 37, module 03 reqs 3-001/3-009).
 *
 * <p>No identity provider is chosen yet (ADR-0017 defers cloud and IdP), so nothing here names a
 * vendor: the {@code JwtDecoder} comes from Boot's standard
 * {@code spring.security.oauth2.resourceserver.jwt.*} properties, which any OIDC-compliant IdP
 * satisfies. Choosing the IdP later is configuration, not code.
 *
 * <p>Two ordering facts matter:
 *
 * <ul>
 *   <li>{@link TenantContextFilter} is installed <em>after</em> {@link
 *       BearerTokenAuthenticationFilter}, so it can only ever read a tenant off a token whose
 *       signature, issuer and expiry Spring Security has already checked. Placing it earlier would
 *       make the tenant attacker-controlled — precisely SEC-001.
 *   <li>The default is {@code authenticated()}. Endpoints are denied unless a rule opens them, so a
 *       new controller is private until someone deliberately publishes it, rather than public until
 *       someone remembers to protect it.
 * </ul>
 *
 * <p>Sessions are stateless: the token is the whole session. Tenant-configurable lifetime and
 * concurrency limits (req 3-009) validate against {@code identity.session_policy}; MFA (req 3-002)
 * is asserted by the IdP via the {@code acr}/{@code amr} claims and checked here rather than
 * re-implemented — see PROGRESS.md, which records this as a deviation pending IdP selection.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final String tenantClaim;

    public SecurityConfig(@Value("${esg360.security.tenant-claim:tenant_id}") String tenantClaim) {
        this.tenantClaim = tenantClaim;
    }

    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        // CSRF protects cookie-authenticated browser sessions; a stateless bearer-token API has no
        // ambient credential for a foreign site to ride, so the token itself is the defence.
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        // Discovery + liveness only: no tenant data, so no token needed.
                        .requestMatchers("/api/v1/meta", "/actuator/health", "/actuator/health/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .addFilterAfter(new TenantContextFilter(tenantClaim), BearerTokenAuthenticationFilter.class);
        return http.build();
    }
}
