package com.example.kakao.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import static com.example.kakao.security.jwt.JwtProperties.*;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    private final ObjectMapper objectMapper;
    private static SecretKey key;

    @PostConstruct
    public void init() {
        String base64EncodedKey = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        key = Keys.hmacShaKeyFor(base64EncodedKey.getBytes());
    }

    public String createToken(Long id, String tokenType) {
        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(tokenType);
        claims.put("user-id", id);

        long expirationTime;
        if (tokenType.equals(ACCESS_TOKEN)) {
            expirationTime = ACCESS_EXPIRES_IN_MILLISECONDS;
        } else {
            expirationTime = REFRESH_EXPIRES_IN_MILLISECONDS;
        }
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationTime))
                .setIssuer("3D")
                .signWith(key)
                .compact()
                ;
    }

    public Object getClaimValue(String token, String claimKey) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get(claimKey);
    }

    public Boolean isValidToken(String token){
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();

        try{
            parser.parseClaimsJws(token);
            return true;

        } catch (Exception e){
            return false;
        }
    }

    public Long decodeTokenForId(String token){
        String[] tokenParts = token.split("\\.");
        String decodedClaim = new String(
                Base64.getDecoder().decode(tokenParts[1]),
                StandardCharsets.UTF_8);
        try {
            Map decodeClaims = objectMapper.readValue(decodedClaim,Map.class);
            return ((Integer) decodeClaims.get("member-id")).longValue();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("INVALID TOKEN");
        }
    }
}
