package com.vigyanmancha.backend.service.security;

import com.vigyanmancha.backend.dto.response.JwtResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtUtils {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication, JwtResponse jwtResponse) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .addClaims(getClaims(jwtResponse))
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    private Map<String, Object> getClaims(JwtResponse jwtResponse) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", jwtResponse.getRoles());
        claims.put("id", jwtResponse.getId());
        claims.put("username", jwtResponse.getUserName());
        claims.put("isAdminUser", jwtResponse.isAdminUser());
        claims.put("isVigyanKendraUser", jwtResponse.isVigyanKendraUser());
        claims.put("isSchoolUser", jwtResponse.isSchoolUser());
        claims.put("vigyanKendraId", jwtResponse.getVigyanKendraId());
        claims.put("vigyanKendraName", jwtResponse.getVigyanKendraName());
        claims.put("vigyanKendraCode", jwtResponse.getVigyanKendraCode());
        return claims;
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public Map<String, Object> getClaims(String token) {
        return getAllClaims(token);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }
}
