package com.github.cloud_transfer.cloud_transfer_backend.security;

import com.github.cloud_transfer.cloud_transfer_backend.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenIssuer {

    private final JwtConfig jwtConfig;

    @Autowired
    public JwtTokenIssuer(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String issueFor(User user) {
        Long now = System.currentTimeMillis();
        Key key = Keys.hmacShaKeyFor(jwtConfig.getSecret());
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("authorities", List.of())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtConfig.getExpiration() * 1000))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}
