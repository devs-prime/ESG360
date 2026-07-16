package com.esg360.web.tenancy;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Populates {@link TenantContext} from the tenant claim of the <em>validated</em> JWT, for the
 * duration of one request.
 *
 * <p>Must be registered after authentication in the filter chain: it reads the {@link
 * SecurityContextHolder}, so it depends on Spring Security having already verified the token's
 * signature, issuer and expiry. Reading the claim off an unverified token would make the tenant
 * caller-supplied, which is the exact defect SEC-001 tests for.
 *
 * <p>The context is cleared in a finally block without exception. Servlet threads are pooled, so a
 * context that outlives its request becomes the next request's tenant — a cross-tenant read caused
 * by nothing but a missing cleanup.
 */
public class TenantContextFilter extends OncePerRequestFilter {

    private final String tenantClaim;

    public TenantContextFilter(String tenantClaim) {
        this.tenantClaim = tenantClaim;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            resolveTenant().ifPresent(TenantContext::set);
            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }

    /**
     * The tenant from the validated token, or empty. Empty is safe: no context means the
     * transaction manager binds no tenant and RLS returns nothing (fail closed). A malformed claim
     * is treated as empty rather than throwing — the token is rejected up front by the resource
     * server's tenant-claim validator, so reaching here with a bad claim is already abnormal, and
     * denying quietly beats leaking claim contents into an error response.
     */
    private Optional<UUID> resolveTenant() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof JwtAuthenticationToken jwt) || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        String claim = jwt.getToken().getClaimAsString(tenantClaim);
        if (claim == null || claim.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(UUID.fromString(claim));
        } catch (IllegalArgumentException notAUuid) {
            return Optional.empty();
        }
    }
}
