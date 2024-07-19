package com.security.authentication.services;

import com.security.authentication.entities.Users;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;

@Service
public class JwtService {

    public static final String SECRET = "oQeuy8oYiC2O+084KXHDwLV8VkcytdZhgXJeh1NaBzpOtb9EBW8jKdX1Homo4MEUGLTBo1mYF2nfaIiQabfeTzSHRncG+cBnUB5smlNwBX/tY2csC4c9TaCQexGAR+MV+th3gVYTUGUxjLmGENYosiyAvyJNEhO0641SInzpbk2T+TV44YjWsx6DFlnKvfJp";

    public String generateToken(Users users) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", populateAuthorities(users.getAuthorities()));
        return createToken(users.getEmail(), claims);
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> grantedAuthorities) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority grantedAuthority : grantedAuthorities)
            authoritiesSet.add(grantedAuthority.getAuthority());
        return String.join(",", authoritiesSet);
    }


    private String createToken(String userName, Map<String, Object> claims) {
        System.out.println(userName);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();

    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
