package com.backupreality.shared.security;

import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class TokenProviderService
{
    private static final String CLAIMS_KEY_ROLES = "roles";
    private static final String CLAIMS_KEY_SECRET = "tsec";


    public static TokenProviderService simpleHMAC(
            KeyData hmacSecret
    )
    {
        SignatureAlgorithm alg = SignatureAlgorithm.HS256;
        Key key = new SecretKeySpec(hmacSecret.getData(), alg.getJcaName());
        return new TokenProviderService(
                alg, key, key
        );
    }



    private final SignatureAlgorithm alg;
    private final Key signKey;
    private final Key verifyKey;


    public TokenProviderService(
            SignatureAlgorithm alg,
            Key signKey,
            Key verifyKey
    )
    {
        this.alg = alg;
        this.signKey = signKey;
        this.verifyKey = verifyKey;
    }


    public String generateToken(TokenData data, Duration duration)
    {
        Claims claims = Jwts.claims();
        data.userID.ifPresent((uid) -> claims.setSubject(uid));
        data.secret.ifPresent((tsig) -> claims.put(CLAIMS_KEY_SECRET, tsig));
        data.authorities.ifPresent((authlist) -> claims.put(
                CLAIMS_KEY_ROLES,
                authlist
        ));

        Instant now = Instant.now();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(duration)))
                .signWith(alg, signKey)
                .compact();
    }


    @SuppressWarnings("unchecked")
    public TokenData parseToken(String token)
    {
        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(verifyKey)
                .parseClaimsJws(token);

        Claims claims = jws.getBody();

        TokenData.Builder builder = TokenData.builder();

        Optional.ofNullable(claims.getSubject())
                .ifPresent((uid) -> builder.setUserID(uid));
        Optional.ofNullable(claims.get(CLAIMS_KEY_SECRET, String.class))
                .ifPresent((sec) -> builder.setSecret(sec));

        Optional.ofNullable(claims.get(CLAIMS_KEY_ROLES, List.class))
                .map((l) -> (List<Object>) l)
                .map((lo) -> lo
                        .stream()
                        .map((roleObject) -> {

                            return Optional.of(roleObject)
                                    .filter((obj) -> obj instanceof String)
                                    .map((obj) -> String.class.cast(obj))
                                    .orElseThrow(() -> {

                                        return new UnsupportedJwtException("Role <" + roleObject + "> is not a string");
                                    });
                        })
                        .collect(Collectors.toList()))
                .ifPresent((ls) -> builder.setAuthoritiesRaw(ls));

        return builder.build();
    }
}

