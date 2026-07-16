package com.esg360.app.testsupport;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

/**
 * Lets integration tests present <em>genuinely signed</em> tokens without an identity provider.
 *
 * <p>The keypair is generated in-process on first use and never leaves the JVM. That is deliberate:
 * committing a test private key would put real key material in the repository, trip secret scanning,
 * and teach exactly the habit this codebase should not teach. Nothing here is on the production
 * classpath — {@code src/test} only.
 *
 * <p>Tests get a real {@link JwtDecoder} verifying real RSA signatures, so security behaviour under
 * test is the production code path rather than a mock that would pass regardless. A test that mints
 * a token for tenant A genuinely cannot read tenant B's rows, which is the point of SEC-001.
 */
@TestConfiguration
public class TestIdentityConfig {

    public static final String ISSUER = "https://test-idp.esg360.invalid";
    private static final KeyPair KEY_PAIR = generateKeyPair();

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey((RSAPublicKey) KEY_PAIR.getPublic())
                .build();
    }

    /** A signed token carrying the tenant claim the {@code TenantContextFilter} reads. */
    public static String tokenFor(UUID tenantId, UUID subject, String... scopes) {
        return mint(builder -> builder.subject(subject.toString())
                .claim("tenant_id", tenantId.toString())
                .claim("scope", String.join(" ", scopes))
                // Asserts the IdP performed MFA (req 3-002). Tests that need to prove the
                // no-MFA path is refused mint their own token without these.
                .claim("acr", "mfa")
                .claim("amr", List.of("pwd", "otp")));
    }

    /** Escape hatch for tests that need a deliberately malformed or unusual token. */
    public static String mint(java.util.function.UnaryOperator<JWTClaimsSet.Builder> customiser) {
        Instant now = Instant.now();
        JWTClaimsSet.Builder claims = new JWTClaimsSet.Builder()
                .issuer(ISSUER)
                .issueTime(java.util.Date.from(now))
                .expirationTime(java.util.Date.from(now.plus(10, ChronoUnit.MINUTES)));
        SignedJWT jwt = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).build(),
                customiser.apply(claims).build());
        try {
            jwt.sign(new RSASSASigner((RSAPrivateKey) KEY_PAIR.getPrivate()));
        } catch (JOSEException e) {
            throw new IllegalStateException("Could not sign test token", e);
        }
        return jwt.serialize();
    }

    private static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("RSA unavailable", e);
        }
    }
}
