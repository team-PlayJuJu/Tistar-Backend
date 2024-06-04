package com.juju.tistar.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

@Component
public class TokenProvider {

    private final UserDetailsService userDetailsService;
    private final String secretKey;
    private final Long access;
    private final Long refresh;

    public TokenProvider(UserDetailsService userDetailsService, @Value("${jwt.secret}") String secretKey,
                         @Value("${jwt.access}") Long access,
                         @Value("${jwt.refresh}") Long refresh){
        this.userDetailsService = userDetailsService;
        this.secretKey = secretKey;
        this.access = access;
        this.refresh = refresh;
    }

    public String generateAccessToken(Authentication authentication) {
        return generateToken(
                authentication.getPrincipal().toString(),
                access
        );
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateToken(
                authentication.getPrincipal().toString(),
                refresh
        );
    }

    public Authentication getAuthentication(String accessToken) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(accessToken));
        return new UsernamePasswordAuthenticationToken(getUsername(accessToken), "", userDetails.getAuthorities());
    }

    public String generateToken(String username, Long expired) {
        Long now = new Date().getTime();

        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(now + expired))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public String resolveToken(String token) {
        if(token != null && token.startsWith("Bearer ")){
            return token.substring(7);
        } else return null;
    }


    private String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public boolean validateToken(String token) {
        if (token == null) {
            return false;
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .after(new Date());
        }
        catch (Exception e) {
            return false;
        }
    }
}
