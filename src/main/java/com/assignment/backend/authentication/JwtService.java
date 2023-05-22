package com.assignment.backend.authentication;

import com.assignment.backend.entity.User;
import com.assignment.backend.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("secret")
    private String secret;

    @Value("3600")
    private long expiration;

    private String key = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";

    @Autowired
    UserService userService;


    public String generateToken(String username, String password) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);
        claims.put("pwd", password);
        claims.put("iat", new Date().getTime());
        claims.put("exp", new Date().getTime() + expiration * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            String tokenUsername = claims.getSubject();
            String tokenPassword = (String) claims.get("pwd");
            System.out.println("Jwt service got username and pword: " + tokenUsername + " " + tokenPassword);
            User user = userService.validateUser(tokenUsername, tokenPassword);
            if(user != null) {
                return true;
            }
            else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(Claims claims) {
        Date expirationDate = claims.getExpiration();
        return expirationDate.before(new Date());
    }
}
